package snowy.server

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, SourceQueue}
import snowy.GameClientProtocol.{Died, GameClientMessage, Ping, State}
import snowy.GameServerProtocol._
import snowy.server.SnowyServerFixture.connectSinkToServer

object LoadTest {
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem()
    import actorSystem.dispatcher

    val testDuration = 1 hour

    val port       = 9000
    val wsUrl      = s"ws://localhost:${port}/game"
    val numClients = 5
    (1 to numClients).foreach { _ =>
      new SimulatedClient(wsUrl)
    }
    Thread.sleep(testDuration.toMillis)
  }
}

object SimulatedClient {
  val testUserId = new AtomicInteger()
}

import snowy.server.SimulatedClient.testUserId

class SimulatedClient(url: String)(implicit executionContext: ExecutionContext) {
  val flow =
    Flow[GameClientMessage].map { message =>
      val send = optSend.get
      sendRandomMessage(send)
      message match {
        case Died     => send.offer(ReJoin())
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
    val random = ThreadLocalRandom.current.nextDouble()
    if (alive) {
      random match {
        case _ if random < .005 => send.offer(Start(Left))
        case _ if random < .010 => send.offer(Start(Right))
        case _ if random < .030 => send.offer(Stop(Right)); send.offer(Stop(Right))
        case _ if random < .040 => send.offer(Start(Push))
        case _ if random < .060 => send.offer(Stop(Push))
        case _ if random < .080 => send.offer(Shoot)
        case _ if random < .090 =>
          send.offer(TurretAngle(ThreadLocalRandom.current.nextDouble() * math.Pi * 2))
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
