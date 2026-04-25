package snowy.load

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.*
import akka.stream.scaladsl.*
import akka.stream.{Materializer, OverflowStrategy}
import akka.util.ByteString
import snowy.GameClientProtocol.{ClientPong, Died, GameClientMessage, Ping}
import snowy.GameServerProtocol.GameServerMessage
import snowy.util.ActorTypes.*
import upickle.default.{readBinary, writeBinary}

import java.nio.ByteBuffer
import scala.concurrent.{ExecutionContextExecutor, Future}

object SnowyClientSocket {
  private val unpickleMessage: Flow[Message, GameClientMessage, ?] = {
    Flow[Message].collect { case BinaryMessage.Strict(msg) =>
      readBinary[GameClientMessage](msg.asByteBuffer)
    }
  }

  private val fastUnpickle = {
    val diedBytes       = ByteBuffer.wrap(writeBinary[GameClientMessage](Died))
    val pingBytes       = ByteBuffer.wrap(writeBinary[GameClientMessage](Ping))
    val clientPongBytes = ByteBuffer.wrap(writeBinary[GameClientMessage](ClientPong))

    Flow[Message]
      .collect { case BinaryMessage.Strict(msg) => msg.toByteBuffer }
      .collect {
        case m if m.compareTo(diedBytes) == 0       => Died
        case m if m.compareTo(pingBytes) == 0       => Ping
        case m if m.compareTo(clientPongBytes) == 0 => Ping
      }
  }

  /** Connect to a Game server WebSocket.
    * @param sink
    *   Sink to be materialized to accept messages from the server
    * @return
    *   a Future containing a SourceQueue for sending messages to the server
    */
  def connectSinkToServer[M: Actors: Measurement](
        wsUrl: String,
        sink: Sink[GameClientMessage, M]
  ): Future[(SourceQueueWithComplete[GameServerMessage], M)] = {
    connectToServer(wsUrl, unpickleMessage, sink)
  }

  /** Connect to a Game server WebSocket. For efficiency when load testing with many
    * clients, only three messages are decoded and passed to the sink: Died, Ping, and
    * ClientPong.
    * @param sink
    *   Sink to be materialized to accept messages from the server
    * @return
    *   a Future containing a SourceQueue for sending messages to the server
    */
  def connectBlindSinkToServer[M: Actors: Measurement](
        wsUrl: String,
        sink: Sink[GameClientMessage, M]
  ): Future[(SourceQueueWithComplete[GameServerMessage], M)] = {
    connectToServer(wsUrl, fastUnpickle, sink)
  }

  private def connectToServer[M: Actors: Measurement](
        wsUrl: String,
        messageConvert: Flow[Message, GameClientMessage, ?],
        sink: Sink[GameClientMessage, M]
  ): Future[(SourceQueueWithComplete[GameServerMessage], M)] = {
    val system                                        = summon[ActorSystem]
    implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
    implicit val materializer: Materializer           = Materializer(system)
    val outputBufferSize                              = 100

    val conversionSink = messageConvert.toMat(sink)(Keep.right)

    val sourceToServer = {
      val sourceQueue =
        Source.queue[GameServerMessage](outputBufferSize, OverflowStrategy.fail)

      val gameMessageToBinaryMessage = Flow[GameServerMessage].map { msg =>
        val msgString = writeBinary[GameServerMessage](msg)
        BinaryMessage(ByteString(msgString)): Message
      }
      sourceQueue.via(gameMessageToBinaryMessage)
    }

    val flow = Flow.fromSinkAndSourceMat(conversionSink, sourceToServer)(Keep.both)

    val (upgradeResponse, (materializedSink, sendQueue)) =
      Http().singleWebSocketRequest(WebSocketRequest(wsUrl), flow)

    upgradeResponse.map { _ =>
      (sendQueue, materializedSink)
    }
  }

}
