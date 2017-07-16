package snowy.server

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.scaladsl._
import akka.stream.{ClosedShape, OverflowStrategy}
import akka.util.ByteString
import boopickle.DefaultBasic.Pickle
import com.typesafe.scalalogging.StrictLogging
import snowy.GameClientProtocol.{GameClientMessage, Ping}
import snowy.playfield.Picklers._
import snowy.util.ActorUtil.materializerWithLogging
import socketserve.ConnectionId

import scala.concurrent.duration._

object ClientConnection {
  val pingMessage = {
    val byteBuffer = Pickle.intoBytes[GameClientMessage](Ping)
    ByteString(byteBuffer)
  }
}

import snowy.server.ClientConnection._

/** track network delay to a client connection */
class ClientConnection(id: ConnectionId,
                       messageIO: MessageIO)(implicit system: ActorSystem)
    extends StrictLogging {
  private implicit val materializer = materializerWithLogging(logger)
  import system.dispatcher

  private val pingFrequency  = 10 seconds
  private var lastPingSent   = 0L
  private var minRecentRtt   = 0L
  private val pingWindowSize = 5
  private val pongsRef       = watchForPongs()

  /** minimum round trip time in the last 5 pings */
  def roundTripTime: Long = minRecentRtt

  sendPing()

  /** The framework message handler should call this to report when
    * a Pong message is received on the server */
  def pongReceived(): Unit = {
    val now      = System.currentTimeMillis()
    val pingTime = now - lastPingSent
    pongsRef ! pingTime
  }

  /** setup a flow to watch for incoming Pongs */
  private def watchForPongs(): ActorRef = {
    val pongSource = Source.actorRef[Long](100, OverflowStrategy.dropTail)

    val graph = GraphDSL.create(pongSource) { implicit builder => pongs =>
      import GraphDSL.Implicits._
      val end = Sink.ignore
      val firstSet = Flow[Long].take(pingWindowSize).map { time =>
        sendPing()
        time
      }
      val ongoing = Flow[Long].drop(pingWindowSize).map { time =>
        system.scheduler.scheduleOnce(pingFrequency) { sendPing() }
        time
      }
      val window = Flow[Long].sliding(pingWindowSize).map { seq =>
        minRecentRtt = seq.min
        logger.trace(s"min ping time for $id is $minRecentRtt")
        minRecentRtt
      }

      val bcast = builder.add(Broadcast[Long](2))
      val merge = builder.add(Merge[Long](2))
      pongs ~> bcast ~> firstSet ~> merge
      bcast ~> ongoing ~> merge ~> window ~> end
      ClosedShape
    }

    val sourceRef: ActorRef = RunnableGraph.fromGraph(graph).run()
    sourceRef
  }

  /** send a ping message to the client */
  private def sendPing(): Unit = {
    logger.trace(s"sendPing $id")
    messageIO.sendBinaryMessage(pingMessage, id)
    lastPingSent = System.currentTimeMillis()
  }

}
