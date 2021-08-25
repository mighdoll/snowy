package snowy.load

import akka.util.ByteString
import boopickle.DefaultBasic.Pickle
import io.netty.buffer.Unpooled
import snowy.GameClientProtocol.GameClientMessage
import snowy.GameServerProtocol.GameServerMessage
import snowy.load.FastUnpickle.partialUnpickleClientMessage
import snowy.playfield.Picklers._

import scala.concurrent.ExecutionContext

class GameSocket(wsUrl: String, messageFn: GameClientMessage => Unit)(implicit
      execution: ExecutionContext
) {
  val socket = NettyWebSocket.connect(wsUrl, receive)

  private def receive(byteString: ByteString): Unit = {
    partialUnpickleClientMessage(byteString).foreach { msg =>
      messageFn(msg)
    }
  }

  def sendMessage(msg: GameServerMessage): Unit = {
    val byteBuffer = Pickle.intoBytes[GameServerMessage](msg)
    val byteBuf    = Unpooled.wrappedBuffer(byteBuffer)
    socket.send(byteBuf)
  }

  def connect(): Unit = {
    socket.connect()
  }
}
