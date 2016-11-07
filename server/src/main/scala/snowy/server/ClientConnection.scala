package snowy.server

import scala.concurrent.duration._
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, ClosedShape, OverflowStrategy}
import akka.util.ByteString
import boopickle.Default._
import snowy.playfield.Picklers._
import com.typesafe.scalalogging.StrictLogging
import snowy.GameClientProtocol.{GameClientMessage, GameTime, Ping}
import socketserve.ConnectionId

object ClientConnection {
  val pingMessage = {
    val byteBuffer = Pickle.intoBytes[GameClientMessage](Ping)
    ByteString(byteBuffer)
  }
}

import snowy.server.ClientConnection._

/** track network delay to a client connection */
class ClientConnection(id: ConnectionId, messageIO: MessageIO)(
      implicit system: ActorSystem)
    extends StrictLogging {
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val pingFrequency = 1 seconds
  var lastPingSent  = 0L
  val pongsRef      = watchForPongs()
  sendPing()

  def pongReceived(): Unit = {
    val now      = System.currentTimeMillis()
    val pingTime = now - lastPingSent
    pongsRef ! pingTime
  }

  /** setup a flow to watch for incoming Pongs
    * sets PingsToHere as a side effect */
  private def watchForPongs(): ActorRef = {
    val pongSource = Source.actorRef[Long](100, OverflowStrategy.dropTail)

    val graph = GraphDSL.create(pongSource) { implicit builder => pongs =>
      import GraphDSL.Implicits._
      val end = Sink.ignore
      val firstSet = Flow[Long].take(5).map { time =>
        sendPing()
        time
      }
      val ongoing = Flow[Long].drop(5).map { time =>
        system.scheduler.scheduleOnce(pingFrequency) { sendPing() }
        time
      }
      val window = Flow[Long].sliding(5).map { seq =>
        val minPing = seq.min
        logger.debug(s"min ping time for $id is $minPing")
        reportRtt(minPing)
        minPing
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

  private def reportRtt(rtt: Long): Unit = {
    val msg = GameTime(System.currentTimeMillis(), (rtt / 2).toInt)
    messageIO.sendMessage(msg, id)
  }

  /** send a ping message to the client */
  private def sendPing(): Unit = {
    messageIO.sendBinaryMessage(pingMessage, id)
    lastPingSent = System.currentTimeMillis()
  }

}
