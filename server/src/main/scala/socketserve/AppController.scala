package socketserve

import scala.concurrent.duration.FiniteDuration
import akka.util.ByteString
import scala.concurrent.duration._

/** An API for simple synchronous server apps that support multiple users over websockets.
  *
  * The synchronous approach is designed to be analogous to the javascript
  * concurrency model.
  *
  * All functions (and any registered tick function) are called synchronously.
  * For example, the framework will never deliver a message() until the previous
  * message() has been processed.
  *
  * The synchronous approach limits the parallelism available to the server
  * application, which has latency and scalability disadvantages. The advantage
  * is that applications are easier to write.
  */
trait AppController {

  /** a new connection is established */
  def open(id: ConnectionId): Unit

  /** an existing connection is gone */
  def gone(id: ConnectionId): Unit

  /** a binary message has been received */
  def message(id: ConnectionId, msg: ByteString): Unit

  /** called to process the next game turn */
  def tick(): Unit = {}

  /** period for game turns */
  def turnPeriod: FiniteDuration = 0 seconds
}

trait AppHostApi {

  /** Broadcast a message to all clients */
  def sendAll(msg: String): Unit

  /** Send a text message to one client */
  def send(msg: String, id: ConnectionId): Unit

  /** Send a binary message to one client */
  def sendBinary(msg: ByteString, id: ConnectionId): Unit
}
