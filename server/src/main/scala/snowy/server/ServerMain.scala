package snowy.server

import com.typesafe.scalalogging.LazyLogging
import snowy.server.CommandLine.BasicArgs
import snowy.util.MeasurementRecorder
import socketserve.WebServer.socketApplication

object ServerMain extends LazyLogging {

  val cmdLineParser = CommandLine.parser("snowy")

  def main(args: Array[String]): Unit = {
    System.setProperty(
      "java.util.logging.manager",
      "org.apache.logging.log4j.jul.LogManager"
    )
    cmdLineParser.parse(args, BasicArgs()).foreach { config =>
      run(config)
    }
  }

  def run(cmdLine: BasicArgs): Unit = {
    cmdLine.conf.foreach(GlobalConfig.addConfigFiles(_))
    logger.info("starting server")

    socketApplication { (api, system) =>
      val measurementRecorder = MeasurementRecorder(GlobalConfig.config)(system)
      new GameControl(api)(system, measurementRecorder)
    }
  }

}
