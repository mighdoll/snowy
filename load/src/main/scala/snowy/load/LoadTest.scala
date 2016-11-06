package snowy.load

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, SourceQueue}
import snowy.GameClientProtocol.{Died, GameClientMessage, Ping, State}
import snowy.GameServerProtocol._
import snowy.server.CommandLine.BasicArgs
import snowy.server.{CommandLine, GlobalConfig}
import snowy.load.SnowyServerFixture.connectSinkToServer

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

    val port       = GlobalConfig.config.getInt("snowy.server.port")
    val wsUrl      = s"ws://localhost:${port}/game"
    val numClients = 20
    (1 to numClients).foreach { _ =>
      new SimulatedClient(wsUrl)
    }
    Thread.sleep(testDuration.toMillis)
  }
}

object SimulatedClient {
  val testUserId = new AtomicInteger()
}
import SimulatedClient.testUserId

class SimulatedClient(url: String)(implicit executionContext: ExecutionContext) {
  val flow =
    Flow[GameClientMessage].map { message =>
      val send = optSend.get
      sendRandomMessage(send)
      message match {
        case Died     => send.offer(ReJoin)
        case s: State => alive = true
        case Ping     => send.offer(Pong)
        case _        =>
      }
      message
    }
  val sink: Sink[GameClientMessage, _] = flow.to(Sink.ignore)
  // cheat to get the toServer queue available to the fromServer flow
  var optSend: Option[SourceQueue[GameServerMessage]] = None
  var alive                                           = false

  def sendRandomMessage(send: SourceQueue[GameServerMessage]): Unit = {
    val gameTime = System.currentTimeMillis()
    val random = ThreadLocalRandom.current.nextDouble()
    if (alive) {
      random match {
        case _ if random < .005 => send.offer(Start(Left, gameTime))
        case _ if random < .010 => send.offer(Start(Right, gameTime))
        case _ if random < .030 => send.offer(Stop(Right, gameTime)); send.offer(Stop(Right, gameTime))
        case _ if random < .040 => send.offer(Start(Pushing, gameTime))
        case _ if random < .060 => send.offer(Stop(Pushing, gameTime))
        case _ if random < .070 =>
          send.offer(TurretAngle(ThreadLocalRandom.current.nextDouble() * math.Pi * 2))
        case _ if random < .120 => send.offer(Shoot(gameTime))
        case _ =>
      }
    }
  }

  connectSinkToServer(url, sink).foreach {
    case (sendQueue, m) =>
      optSend = Some(sendQueue)

      val userName = s"testUser-${testUserId.getAndIncrement}"
      sendQueue.offer(Join(userName))
  }

}
