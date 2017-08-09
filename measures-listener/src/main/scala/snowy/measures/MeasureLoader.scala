package snowy.measures

import java.io.File
import java.nio.file.{Files, Path, Paths}
import scala.concurrent.{Await, Future}
import akka.actor.ActorSystem
import com.typesafe.scalalogging.StrictLogging
import scopt.OptionParser
import snowy.util.ActorUtil.materializerWithLogging
import snowy.measures.IngestTsvFile.ingestTsv
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import snowy.util.FutureAwaiting._

object MeasureLoader extends StrictLogging {
  def main(args: Array[String]): Unit = {
    implicit val system       = ActorSystem()
    implicit val materializer = materializerWithLogging(logger)
    import system.dispatcher

    val results: Option[Future[Unit]] = {
      for {
        cmdLine <- parser.parse(args, CmdLine())
        tsvFile <- cmdLine.tsvFile
      } yield {
        ingestTsv(tsvFile.toPath).map { ingestResults =>
          import ingestResults.{gauges, spans}
          println(s"loaded: $spans spans  and $gauges gauges")
          Unit
        }
      }
    }

    val returnCode =
      results match {
        case Some(future) =>
          Try {
            future.await(1.minute)
          }.map(_ => 0).getOrElse(-1)
        case None =>
          parser.showUsageAsError()
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
