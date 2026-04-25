package snowy.server

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.scaladsl.*
import akka.stream.{ClosedShape, CompletionStrategy, Materializer, OverflowStrategy}
import akka.util.ByteString
import upickle.default.writeBinary
import scala.concurrent.ExecutionContext.Implicits.global
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import snowy.GameClientProtocol.{GameClientMessage, Ping}
import snowy.util.ActorUtil.materializerWithLogging
import socketserve.ConnectionId

import scala.concurrent.duration.*
import scala.language.postfixOps

object ClientConnection {
  val pingMessage = {
    val byteBuffer = writeBinary[GameClientMessage](Ping)
    ByteString(byteBuffer)
  }
}

import snowy.server.ClientConnection.*

/** track network delay to a client connection */
class ClientConnection(id: ConnectionId, messageIO: MessageIO)(implicit
      system: ActorSystem
) extends Logging {
  private implicit val materializer: Materializer = materializerWithLogging(logger)

  private val pingFrequency  = 10 seconds
  private var lastPingSent   = 0L
  private var minRecentRtt   = 0L
  private val pingWindowSize = 5
  private val pongsRef       = watchForPongs()

  /** minimum round trip time in the last 5 pings */
  def roundTripTime: Long = minRecentRtt

  sendPing()

  /** The framework message handler should call this to report when a Pong message is
    * received on the server
    */
  def pongReceived(): Unit = {
    val now      = System.currentTimeMillis()
    val pingTime = now - lastPingSent
    pongsRef ! pingTime
  }

  /** setup a flow to watch for incoming Pongs */
  private def watchForPongs(): ActorRef = {
    val pongSource = Source.actorRef[Long](
      completionMatcher = PartialFunction.empty,
      failureMatcher = PartialFunction.empty,
      bufferSize = 100,
      overflowStrategy = OverflowStrategy.dropTail
    )

    val graph = RunnableGraph.fromGraph(GraphDSL.createGraph(pongSource) {
      implicit builder => (pongs) =>
        import akka.stream.scaladsl.GraphDSL.Implicits.*

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
    })

    val sourceRef: ActorRef = graph.run()
    sourceRef
  }

  /** send a ping message to the client */
  private def sendPing(): Unit = {
    logger.trace(s"sendPing $id")
    messageIO.sendBinaryMessage(pingMessage, id)
    lastPingSent = System.currentTimeMillis()
  }

}
