import GameClientProtocol.Ping
import socketserve.{AppHostApi, ConnectionId}
import upickle.default._

object ClientConnection {
  val pingMessage = write(Ping)
}

import ClientConnection._

/** track network delay to a client connection */
class ClientConnection(id: ConnectionId, api: AppHostApi) {
  var minPingTime: Option[Long] = None
  var lastPingSent = 0L
  var pingsSent = 0

  sendPing()

  /** called when a pong message is received from the client */
  def pongReceived(): Unit = {
    val pingTime = System.currentTimeMillis() - lastPingSent
    minPingTime = minPingTime.map { currentMin =>
      math.min(currentMin, pingTime)
    }.orElse(Some(pingTime))

    if (pingsSent < 5) {
      sendPing()
    } else {
      println(s"ClientConnection.minPingTime: ${minPingTime.get}")
    }
  }

  /** send a ping message to the client */
  private def sendPing(): Unit = {
    api.send(pingMessage, id)
    lastPingSent = System.currentTimeMillis()
    pingsSent = pingsSent + 1
  }
}
