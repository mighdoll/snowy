package snowy.server
import java.io.File

import scopt.OptionParser

object CommandLine {

  /** @return a scopt parser that handles the --conf option */
  def parser(programName: String): OptionParser[BasicArgs] = {
    new scopt.OptionParser[BasicArgs](programName) {
      head("snowy", "0.1")

      opt[File]("conf")
        .valueName("<file>")
        .validate { file =>
          if (file.canRead()) Right(())
          else Left(s"${file.toString} not readable")
        }
        .action((file, config) => config.copy(conf = Some(file)))
        .text("config file to parse")

      help("help").text("prints this usage text")
    }
  }

  /** Program arguments if the only option is --conf */
  case class BasicArgs(conf: Option[File] = None)
}
