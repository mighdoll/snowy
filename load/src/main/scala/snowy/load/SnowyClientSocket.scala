package snowy.load

import scala.concurrent.Future
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws._
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.util.ByteString
import boopickle.Default._
import snowy.GameClientProtocol.GameClientMessage
import snowy.GameServerProtocol.GameServerMessage
import socketserve.ActorTypes._
import snowy.playfield.Picklers._

object SnowyClientSocket {

  /** Connect to a Game server websocket.
    * @param sink Sink to be materialized to accept messages from the server
    * @return a Future containing a SourceQueue for sending messages to the server
    */
  def connectSinkToServer[M: Actors](
        wsUrl: String,
        sink: Sink[GameClientMessage, M]
  ): Future[(SourceQueueWithComplete[GameServerMessage], M)] = {
    implicit val dispatcher = implicitly[ActorSystem].dispatcher
    implicit val _          = ActorMaterializer()
    val outputBufferSize    = 100

    val sinkFromServer = {
      val messageToGameMessage = Flow[Message].collect {
        case BinaryMessage.Strict(msg) =>
          Unpickle[GameClientMessage].fromBytes(msg.asByteBuffer)
      }
      messageToGameMessage.toMat(sink)(Keep.right)
    }

    val sourceToServer = {
      val sourceQueue =
        Source.queue[GameServerMessage](outputBufferSize, OverflowStrategy.fail)

      val gameMessageToBinaryMessage = Flow[GameServerMessage].map { msg =>
        val msgString = Pickle.intoBytes[GameServerMessage](msg)
        BinaryMessage(ByteString(msgString)): Message
      }
      sourceQueue.via(gameMessageToBinaryMessage)
    }

    val flow = Flow.fromSinkAndSourceMat(sinkFromServer, sourceToServer)(Keep.both)

    val (upgradeResponse, (materializedSink, sendQueue)) =
      Http().singleWebSocketRequest(WebSocketRequest(wsUrl), flow)

    upgradeResponse.map { _ =>
      (sendQueue, materializedSink)
    }
  }
}
