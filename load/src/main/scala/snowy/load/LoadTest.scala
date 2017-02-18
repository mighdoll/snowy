package snowy.load

import scala.concurrent.duration._
import akka.actor.ActorSystem
import snowy.server.CommandLine.BasicArgs
import snowy.server.{CommandLine, GlobalConfig}

object LoadTest {

  def main(args: Array[String]): Unit = {
    val cmdLineParser = CommandLine.parser("snowy-loadtest")
    cmdLineParser.parse(args, BasicArgs()).foreach { basicArgs =>
      run(basicArgs)
    }
  }

  def run(basicArgs: BasicArgs) {
    basicArgs.conf.foreach(GlobalConfig.addConfigFiles(_))

    val actorSystem = ActorSystem()
    import actorSystem.dispatcher

    val testDuration = 1 hour

    val config     = GlobalConfig.config
    val port       = config.getInt("snowy.server.port")
    val wsUrl      = s"ws://localhost:${port}/game"
    val numClients = config.getInt("snowy.load.clients")
    (1 to numClients).foreach { _ =>
      new SingleLoadTestClient(wsUrl)
    }
    Thread.sleep(testDuration.toMillis)
  }
}

