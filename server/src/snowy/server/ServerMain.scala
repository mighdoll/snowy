package snowy.server

//import com.typesafe.scalalogging.LazyLogging
import scribe.Logging
import snowy.server.CommandLine.BasicArgs
import socketserve.WebServer.socketApplication

object ServerMain extends Logging {

  val cmdLineParser = CommandLine.parser("snowy")

  def main(args: Array[String]): Unit = {
    cmdLineParser.parse(args, BasicArgs()).foreach { config =>
      run(config)
    }
  }

  def run(cmdLine: BasicArgs): Unit = {
    cmdLine.conf.foreach(GlobalConfig.addConfigFiles(_))
    logger.info("starting server")

    socketApplication { (api, system, appSpan) =>
      new GameControl(api, system, appSpan, GlobalConfig.snowy)
    }
  }

}
