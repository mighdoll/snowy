package snowy.util

import java.nio.file.StandardOpenOption.{CREATE, TRUNCATE_EXISTING, WRITE}
import java.nio.file.{Files, Path, Paths}
import akka.actor.ActorSystem
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{FileIO, Source, SourceQueueWithComplete}
import akka.util.ByteString
import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging
import socketserve.ActorUtil.materializerWithLogging
import socketserve.FlowImplicits._

object MeasurementRecorder {
  def apply(config: Config)(implicit system: ActorSystem): MeasurementRecorder = {
    if (config.getBoolean("snowy.measurement.enable")) {
      val directory = config.getString("snowy.measurement.directory")
      new MeasurementToTsvFile(directory)
    } else {
      NullMeasurementRecorder
    }
  }
}

/** a measurement recording system, allows publishing duration Span measurements to various backends */
trait MeasurementRecorder {
  def publish(measurement: Measurement)
  def close(): Unit = {}
}

object NullMeasurementRecorder extends MeasurementRecorder {
  override def publish(measurement: Measurement) = {}
}

/** a measurement system that sends measurements to a file */
class MeasurementToTsvFile(directoryName: String)(implicit system: ActorSystem)
    extends MeasurementRecorder with StrictLogging {
  implicit val materializer = materializerWithLogging(logger)
  val path                  = Paths.get(directoryName)
  val spans = startTsvFile(
    path.resolve("spans.tsv"),
    "name\tspanId\tparentId\tstartEpochMicros\tdurationMicros\n"
  )

  override def close(): Unit = {
    spans.complete()
  }

  override def publish(measurement: Measurement): Unit = {
    measurement match {
      case span: CompletedSpan => publishSpan(span)
    }
  }

  private def publishSpan(span: CompletedSpan): Unit = {
    val name        = span.name
    val startMicros = span.start.value
    val duration    = span.end.value - startMicros
    val spanId      = span.id.value.toHexString
    val parentId    = span.parent.map(_.id.value.toHexString).getOrElse("_")
    val csv         = s"$name\t$spanId\t$parentId\t$startMicros\t$duration\n"
    spans.offer(csv)
  }

  private def startTsvFile(path: Path, header: String): SourceQueueWithComplete[String] = {
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
}
