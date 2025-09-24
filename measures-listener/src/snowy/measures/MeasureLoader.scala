package snowy.measures

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import java.io.File
import scala.concurrent.Future
//import com.typesafe.scalalogging.StrictLogging
import scopt.OptionParser
import scribe.Logging
import snowy.measures.IngestTsvFile.ingestTsv
import snowy.util.ActorUtil.materializerWithLogging
import snowy.util.FutureAwaiting.*

import scala.concurrent.duration.*
import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits.global

object MeasureLoader extends Logging {
  def main(args: Array[String]): Unit = {
    implicit val system                          = ActorSystem()
    implicit val materializer: ActorMaterializer = materializerWithLogging(logger)

    val results: Option[Future[Unit]] = {
      for {
        cmdLine <- parser.parse(args, CmdLine())
        tsvFile <- cmdLine.tsvFile
      } yield {
        ingestTsv(tsvFile.toPath).map { ingestResults =>
          val spans  = ingestResults.spans
          val gauges = ingestResults.gauges
          println(s"loaded: $spans spans  and $gauges gauges")
        }
      }
    }

    results match {
      case Some(future) =>
        Try {
          future.await(1.minute)
        }.map(_ => 0).getOrElse(-1)
      case None =>
        parser.displayToErr(parser.usage)
        -1
    }
    system.terminate()
//    sys.exit(returnCode)  // breaks sbt..
  }

  lazy val parser =
    new OptionParser[CmdLine]("measure-loader") {
      head("measure-loader", "0.1")

      opt[File]("tsv")
        .valueName("<filePath.tsv>")
        .validate { file =>
          if (file.canRead()) Right(())
          else Left(s"${file.toString} not readable")
        }
        .action((file, cmdLine) => cmdLine.copy(tsvFile = Some(file)))
        .text(".tsv file to load")

      help("help").text("prints this usage text")
    }

  case class CmdLine(tsvFile: Option[File] = None)

}
