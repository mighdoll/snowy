package snowy.server

import akka.util.ByteString
import boopickle.DefaultBasic.{Pickle, PickleState}
import boopickle.{BufferPool, EncoderSize}
import com.typesafe.scalalogging.StrictLogging
import snowy.GameClientProtocol._
import snowy.playfield.Picklers._
import socketserve.{AppHostApi, ConnectionId}

/** A GameClientMessage wrapper over the send/receive api */
class MessageIO(api: AppHostApi) extends StrictLogging {
  // optimization for boopickle: don't track references, since we don't send any graphs
  implicit def pickleState = new PickleState(new EncoderSize, false, false)

  /** Send a message to the client */
  def sendMessage(message: GameClientMessage, id: ConnectionId): Unit = {
    logInterestingMessages(message, id)

    val bytes      = Pickle.intoBytes(message)
    val byteString = ByteString(bytes)

    sendBinaryMessage(byteString, id)

    BufferPool.release(bytes)
  }

  def sendBinaryMessage(byteString: ByteString, id: ConnectionId): Unit = {
    api.sendBinary(byteString, id)
  }

  private def logInterestingMessages(message: GameClientMessage, id: ConnectionId): Unit = {
//    logger.trace(s"sending message: $message  to: $id")
    message match {
      case Ping          =>
      case ClientPong    =>
      case _: GameTime   =>
      case _: State      =>
      case _: Scoreboard =>
      case m             => logger.trace(s"sending message: $message  to: $id")
    }

  }
}
