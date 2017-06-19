package snowy.load

import akka.util.ByteString
import com.typesafe.scalalogging.StrictLogging
import io.netty.buffer.ByteBuf
import org.asynchttpclient.ws.{WebSocket, WebSocketListener, WebSocketUpgradeHandler}
import org.asynchttpclient.{DefaultAsyncHttpClient, DefaultAsyncHttpClientConfig}
import socketserve.ActorTypes.Execution

import scala.concurrent.Promise
import scala.util.Success
//import org.asynchttpclient.AsyncHttpClientConfig

object NettyWebSocket extends StrictLogging {

  trait SocketControl {
    def close(): Unit

    def send(msg: ByteBuf): Unit

    def connect(): Unit
  }

  val httpClient = {
    val config = new DefaultAsyncHttpClientConfig.Builder()
      .setWebSocketMaxFrameSize(524288)
      .setMaxConnections(500)
      .setIoThreadsCount(100)
      .setThreadPoolName("NettyWebSocket")
      .build()

    new DefaultAsyncHttpClient(config)
  }

  def connect[_: Execution](wsUrl: String,
                            receivedFn: ByteString => Unit): SocketControl = {

    val promisedSocket = Promise[WebSocket]()
    val futureSocket   = promisedSocket.future

    val listener = new WebSocketListener {
      var incomingBytes = ByteString.empty

      override def onError(t: Throwable): Unit = {
        logger.warn("socket error", t)
      }

      override def onClose(websocket: WebSocket, code: Int, reason: String): Unit = {
        logger.info(s"socket $websocket closed: $code $reason")
      }

      override def onOpen(websocket: WebSocket): Unit = {
        logger.info(s"socket $websocket opened")
        promisedSocket.complete(Success(websocket))
      }

      override def onBinaryFrame(payload: Array[Byte],
                                 finalFragment: Boolean,
                                 rsv: Int): Unit = {

        incomingBytes = incomingBytes ++ payload
        if (finalFragment) {
          val bytes = incomingBytes
          incomingBytes = ByteString.empty
          receivedFn(bytes)
        }
      }
    }

    val wsUpgradeHandler =
      new WebSocketUpgradeHandler.Builder().addWebSocketListener(listener).build()

    val request = httpClient.prepareGet(wsUrl)

    new SocketControl {
      def close(): Unit = futureSocket.foreach(_.sendCloseFrame())

      def send(msg: ByteBuf): Unit = {
        futureSocket.foreach { socket =>
          socket.sendBinaryFrame(msg, true, 0)
        }
      }

      def connect(): Unit = {
        request.execute(wsUpgradeHandler)
      }
    }

  }
}
