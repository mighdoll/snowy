package snowy.load

import akka.actor.ActorSystem
import snowy.server.CommandLine.BasicArgs
import snowy.server.{CommandLine, GlobalConfig}
import scala.concurrent.duration._
import snowy.measures.MeasurementRecorder

object LoadTest {

  def main(args: Array[String]): Unit = {
    val cmdLineParser = CommandLine.parser("snowy-loadtest")
    cmdLineParser.parse(args, BasicArgs()).foreach { basicArgs =>
      run(basicArgs)
    }
  }

  def run(basicArgs: BasicArgs) {
    basicArgs.conf.foreach(GlobalConfig.addConfigFiles(_))

    implicit val actorSystem = ActorSystem()

    val testDuration = 1 hour

    val config                       = GlobalConfig.config
    implicit val measurementRecorder = MeasurementRecorder(config)
    val port                         = config.getInt("snowy.server.port")
    val wsUrl                        = s"ws://localhost:${port}/game"
    val numClients                   = config.getInt("snowy.load.clients")
    (1 to numClients).foreach { _ =>
      new SingleLoadTestClient(wsUrl)
    }
    val timing = new TimingRobot(wsUrl)
    Thread.sleep(testDuration.toMillis)
    timing.shutdown()
  }
}
