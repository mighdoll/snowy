package snowy.measures

import java.nio.file.StandardOpenOption.{CREATE, TRUNCATE_EXISTING, WRITE}
import java.nio.file.{Files, Path, Paths}
import akka.actor.ActorSystem
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{FileIO, Source, SourceQueueWithComplete}
import akka.util.ByteString
import com.typesafe.config.Config
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import snowy.util.PartialMatch._
import snowy.util.FlowImplicits._
import snowy.util.ActorUtil._

object MeasurementRecorder {
  def apply(config: Config)(implicit system: ActorSystem): MeasurementRecorder = {
    if (config.getBoolean("snowy.measurement.enable")) {
      val directory = config.getString("snowy.measurement.directory")
      val baseName  = config.getString("snowy.measurement.base-name")
      new MeasurementToTsvFile(directory, baseName)
    } else {
      NullMeasurementRecorder
    }
  }
}

/** a measurement recording system, allows publishing duration Span measurements to various backends */
trait MeasurementRecorder {
  def publish(measurement: CompletedMeasurement[_]): Unit
  def close(): Unit = {}
}

object NullMeasurementRecorder extends MeasurementRecorder {
  override def publish(measurement: CompletedMeasurement[_]) = {}
}

/** a measurement system that sends measurements to a file */
class MeasurementToTsvFile(directoryName: String, baseName: String)(
      implicit system: ActorSystem
) extends MeasurementRecorder with Logging {
  implicit val materializer = materializerWithLogging(logger)
  val path                  = Paths.get(directoryName)
  val records = startTsvFile(
    path.resolve(s"$baseName.tsv"),
    "recordType\tname\tspanId\tparentId\tstartEpochMicros\tvalue\n"
  )

  override def close(): Unit = {
    records.complete()
  }

  override def publish(measurement: CompletedMeasurement[_]): Unit = {
    measurementValue(measurement) match {
      case Some((recordType, value)) =>
        val name        = measurement.name
        val startMicros = measurement.start.value
        val typeCode    = recordType.code
        val spanId      = measurement.id.value.toHexString
        val parentId    = measurement.parent.map(_.id.value.toHexString).getOrElse("_")
        val csv         = s"$typeCode\t$name\t$spanId\t$parentId\t$startMicros\t$value\n"
        records.offer(csv)
      case None =>
        logger.warn(s"unhandled measurement type: $measurement")
    }
  }

  private def measurementValue(
        measurement: CompletedMeasurement[_]
  ): Option[(RecordType, String)] = {
    measurement pmatch {
      case span: CompletedSpan         => DurationRecord -> measurement.value.toString
      case Gauged(_, value: Int, _)    => LongRecord     -> value.toString
      case Gauged(_, value: Long, _)   => LongRecord     -> value.toString
      case Gauged(_, value: String, _) => StringRecord   -> value.toString
      case Gauged(_, value: Double, _) => DoubleRecord   -> value.toString
    }
  }

  private def startTsvFile(path: Path,
                           header: String): SourceQueueWithComplete[String] = {
    createDirectoriesTo(path)
    val fileStream = {
      val source = Source
        .queue[String](3, OverflowStrategy.dropBuffer)
        .fixedBuffer(8192, logger.warn(s"overflow writing to $path"))
        .map(ByteString(_))
      val sink = FileIO.toPath(path, Set(WRITE, CREATE, TRUNCATE_EXISTING))
      source.to(sink).run()
    }
    fileStream.offer(header)
    fileStream
  }

  private def createDirectoriesTo(path: Path): Unit = {
    val parentDir = path.getParent
    if (!Files.exists(parentDir)) {
      Files.createDirectories(parentDir)
    }
  }

  sealed trait RecordType {
    def code: String
  }
  case object DurationRecord extends RecordType {
    override def code = "S"
  }
  case object LongRecord extends RecordType {
    override def code = "L"
  }
  case object DoubleRecord extends RecordType {
    override def code = "D"
  }
  case object StringRecord extends RecordType {
    override def code = "r"
  }
}
