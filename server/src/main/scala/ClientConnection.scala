import GameClientProtocol.Ping
import socketserve.{AppHostApi, ConnectionId}
import upickle.default._
import scala.concurrent.duration._

object ClientConnection {
  val pingMessage = write(Ping)
}

import ClientConnection._

/** track network delay to a client connection */
class ClientConnection(id: ConnectionId, api: AppHostApi) {
  var minPingTime: Option[Long] = None
  var lastPingSent = 0L
  var pingsSent = 0
  val pingFrequency = 10 seconds
  val initialPings = 5

  sendPing()

  /** called when a pong message is received from the client */
  def pongReceived(): Unit = {
    val now = System.currentTimeMillis()
    val pingTime = now - lastPingSent
    minPingTime = minPingTime.map { currentMin =>
      math.min(currentMin, pingTime)
    }.orElse(Some(pingTime))

    if (initializing) {
      sendPing()
    } else {
      println(s"ClientConnection.minPingTime: ${minPingTime.get}  pingTime:$pingTime")
    }
  }

  private def initializing:Boolean = pingsSent < initialPings

  /** send a ping every pingFrequency seconds */
  def refreshTiming():Unit = {
    val now = System.currentTimeMillis()
    if (!initializing && now > lastPingSent + pingFrequency.toMillis) {
//      println(s"sending ping $now")
      sendPing()
    }
  }

  /** send a ping message to the client */
  private def sendPing(): Unit = {
    api.send(pingMessage, id)
    lastPingSent = System.currentTimeMillis()
    pingsSent = pingsSent + 1
  }
}
