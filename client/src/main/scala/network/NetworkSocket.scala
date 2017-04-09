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

  def onOpen(fn: Event => Unit): Unit = {
    socket.addEventListener("open", fn, false)
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
    socket.addEventListener("error", fn, false)
  }

  def onClose(fn: CloseEvent => Unit): Unit = {
    socket.addEventListener("close", fn, false)
  }

  def onMessage(fn: MessageEvent => Unit): Unit = {
    if (inDelay.length == 0) {
      socket.addEventListener("message", fn, false)
    } else {
      socket.addEventListener("message", { message: MessageEvent =>
        delay(inDelay) {
          fn(message)
        }
      }, false)
    }
  }

  private def delay(time: FiniteDuration)(fn: => Unit): Unit = {
    window.setTimeout(() => fn, time.toMillis)
  }

}
