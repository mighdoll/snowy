package snowy.server

import akka.util.ByteString
import boopickle.Default._
import boopickle.{BufferPool, EncoderSize}
import snowy.GameClientProtocol.GameClientMessage
import snowy.util.Perf.time
import socketserve.{AppHostApi, ConnectionId}
import snowy.playfield.Picklers._

/** A GameClientMessage wrapper over the send/receive api */
class MessageIO(api: AppHostApi) {
  // optimization for boopickle: don't track references, since we don't send any graphs
  implicit def pickleState = new PickleState(new EncoderSize, false, false)

  /** Send a message to the client */
  def sendMessage(message: GameClientMessage, id: ConnectionId): Unit = {

    time("pickling") {
      val bytes      = Pickle.intoBytes(message)
      val byteString = ByteString(bytes)
      time("send") {
        sendBinaryMessage(byteString, id)
      }
      BufferPool.release(bytes)
    }
  }

  def sendBinaryMessage(byteString: ByteString, id: ConnectionId): Unit = {
    api.sendBinary(byteString, id)
  }
}