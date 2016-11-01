package network

import scala.concurrent.duration.FiniteDuration
import org.scalajs.dom._

/** A wrapper around a web socket that supports simulated network delays
  * @param url: connect the web socket to this address
  * @param inDelay: delay message from the server by this amount of time
  * @param outDelay: delay message to the server by this amount of time
  */
class NetworkSocket(url: String, inDelay: FiniteDuration, outDelay: FiniteDuration) {
  val socket = new WebSocket(url)

  socket.binaryType = "arraybuffer"
  private def delay(time: FiniteDuration)(fn: => Unit): Unit = {
    window.setTimeout(() => fn, time.toMillis)
  }

  def onOpen(fn: Event => Unit): Unit = {
    socket.onopen = fn
  }

  def send(data: String): Unit = {
    if (outDelay.length == 0) {
      socket.send(data)
    } else {
      delay(outDelay) {
        socket.send(data)
      }
    }
  }

  def onError(fn: ErrorEvent => Unit): Unit = {
    socket.onerror = fn
  }

  def onClose(fn: CloseEvent => Unit): Unit = {
    socket.onclose = fn
  }

  def onMessage(fn: MessageEvent => Unit): Unit = {
    if (inDelay.length == 0) {
      socket.onmessage = fn
    } else {
      socket.onmessage = { message:MessageEvent =>
        delay(inDelay) {
          fn(message)
        }
      }
    }
  }

}
