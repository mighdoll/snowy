package snowy.server

import java.io.File
import kamon.Kamon
import snowy.server.CommandLine.BasicArgs
import socketserve.WebServer.socketApplication

object ServerMain {

  val cmdLineParser = CommandLine.parser("snowy")

  def main(args: Array[String]): Unit = {
    System.setProperty(
      "java.util.logging.manager",
      "org.apache.logging.log4j.jul.LogManager")
    cmdLineParser.parse(args, BasicArgs()).foreach { config =>
      run(config)
    }
  }

  def run(cmdLine: BasicArgs): Unit = {
    cmdLine.conf.foreach(GlobalConfig.addConfigFiles(_))

    Kamon.start()
    socketApplication { (api, system) =>
      new GameControl(api)(system)
    }
  }

}
