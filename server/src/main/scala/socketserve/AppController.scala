package socketserve

import scala.concurrent.duration.FiniteDuration

/** support for simple apps that support multiple users over websockets */
trait AppController {
  /** a new connection is established */
  def open(id: ConnectionId): Unit

  /** an existing connection is gone */
  def gone(id: ConnectionId): Unit

  /** a text message has been received */
  def message(id: ConnectionId, msg: String): Unit
}

trait AppHostApi {
  /** Broadcast a message to all clients */
  def sendAll(msg: String): Unit

  /** Send a message to one client */
  def send(msg: String, id: ConnectionId): Unit

//  def sendBinary(msg:Array[Byte], id: ConnectionId): Unit

  /** register a function to be called periodically */
  def tick(time: FiniteDuration)(fn: => Unit)
}