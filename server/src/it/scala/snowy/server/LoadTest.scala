package snowy.server

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{ExecutionContext, Future}
import snowy.server.SnowyServerFixture.{ServerTestApi, connectSinkToServer}
import scala.concurrent.duration._
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.FlowShape
import akka.stream.scaladsl.GraphDSL.Builder
import akka.stream.scaladsl.{Flow, GraphDSL, Sink, SourceQueue, SourceQueueWithComplete}
import snowy.GameClientProtocol.{Died, GameClientMessage, Ping, State}
import snowy.GameServerProtocol._

object LoadTest {
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem()
    import actorSystem.dispatcher

    val testDuration = 1 hour

    val port = 2345
    val wsUrl = s"ws://localhost:${port}/game"
    val numClients = 20
    (1 to numClients).foreach {_ =>
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
  // cheat to get the toServer queue available to the fromServer flow
  var optSend: Option[SourceQueue[GameServerMessage]] = None

  var alive = false

  val flow =
    Flow[GameClientMessage]
      .map{  message =>
        val send = optSend.get
        sendRandomMessage(send)
        message match {
          case Died => send.offer(ReJoin)
          case s:State => alive = true
          case Ping => send.offer(Pong)
          case _ =>
        }
        message
      }

  val sink: Sink[GameClientMessage, _] = flow.to(Sink.ignore)

  def sendRandomMessage(send:SourceQueue[GameServerMessage]):Unit = {
    val random = ThreadLocalRandom.current.nextDouble()
    if (alive) {
      random match {
        case _ if random < .005 => send.offer(Shoot)
        case _ =>
      }
    }
  }

  connectSinkToServer(url, sink).foreach { case (sendQueue, m) =>
    optSend = Some(sendQueue)

    val userName = s"testUser-${testUserId.getAndIncrement}"
    sendQueue.offer(Join(userName))
  }


}
