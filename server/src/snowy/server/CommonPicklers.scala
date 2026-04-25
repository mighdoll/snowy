package snowy.server

import akka.util.ByteString
import snowy.GameClientProtocol.GameClientMessage
import upickle.default.writeBinary

object CommonPicklers {

  /** Run a function with a pickled message, then release the message from the picklers
    * buffer pool.
    */
  def withPickledClientMessage[T](message: GameClientMessage)(fn: ByteString => T): T = {
    // optimization for boopickle: don't track references, since we don't send any graphs
    // implicit def pickleState: PickleState = new PickleState(new EncoderSize, false, false)

    val bytes      = writeBinary[GameClientMessage](message)
    val byteString = ByteString(bytes)

    val result = fn(byteString)

    // BufferPool.release(bytes)
    result
  }
}
