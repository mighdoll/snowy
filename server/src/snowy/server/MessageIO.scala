package snowy.server

import akka.util.ByteString
import upickle.default.writeBinary
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import snowy.GameClientProtocol.*
import socketserve.{AppHostApi, ConnectionId}

/** A GameClientMessage wrapper over the send/receive api */
class MessageIO(api: AppHostApi) extends Logging {
  // optimization for boopickle: don't track references, since we don't send any graphs
  // implicit def pickleState: PickleState = new PickleState(new EncoderSize, false, false)

  /** Send a message to the client */
  def sendMessage(message: GameClientMessage, id: ConnectionId): Unit = {
    logInterestingMessages(message, id)

    val bytes      = writeBinary[GameClientMessage](message)
    val byteString = ByteString(bytes)

    sendBinaryMessage(byteString, id)
  }

  def sendBinaryMessage(byteString: ByteString, id: ConnectionId): Unit = {
    api.sendBinary(byteString, id)
  }

  private def logInterestingMessages(
        message: GameClientMessage,
        id: ConnectionId
  ): Unit = {
//    logger.trace(s"sending message: $message  to: $id")
    message match {
      case Ping          =>
      case ClientPong    =>
      case _: GameTime   =>
      case _: State      =>
      case _: Scoreboard =>
      case _             => logger.trace(s"sending message: $message  to: $id")
    }

  }
}
