package snowy.load

import akka.util.ByteString
import io.netty.buffer.Unpooled
import snowy.GameClientProtocol.GameClientMessage
import snowy.GameServerProtocol.GameServerMessage
import snowy.load.FastUnpickle.partialUnpickleClientMessage
import upickle.default.writeBinary

import java.nio.ByteBuffer
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
    val byteBuffer = ByteBuffer.wrap(writeBinary[GameServerMessage](msg))
    val byteBuf    = Unpooled.wrappedBuffer(byteBuffer)
    socket.send(byteBuf)
  }

  def connect(): Unit = {
    socket.connect()
  }
}
