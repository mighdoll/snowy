package snowy.server

import akka.util.ByteString
import boopickle.Default._
import boopickle.{BufferPool, EncoderSize}
import snowy.GameClientProtocol.GameClientMessage
import snowy.playfield.Picklers._

object CommonPicklers {
  /** Run a function with a pickled message, then release the message from the
    * picklers buffer pool.
    */
  def withPickledClientMessage[T](message: GameClientMessage)(fn: ByteString => T): T = {
    // optimization for boopickle: don't track references, since we don't send any graphs
    implicit def pickleState = new PickleState(new EncoderSize, false, false)

    val bytes      = Pickle.intoBytes(message)
    val byteString = ByteString(bytes)

    val result = fn(byteString)

    BufferPool.release(bytes)
    result
  }
}
