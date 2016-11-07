package snowy.connection

import snowy.GameServerProtocol.{GameServerMessage, RequestGameTime}
import org.scalajs.dom.window

/** Track the clients estimate of the servers game clock */
class ServerGameClock(sendMessage: GameServerMessage => Unit) {
  var clientClockOffset = 0

  requestGameTime()
  window.setInterval(() => requestGameTime(), 5000)

  /** Revise the estimate of the server game clock (based on a message
    * from the server) */
  def updateClock(serverTime: Long, oneWayDelay: Int): Unit = {
    val now    = System.currentTimeMillis()
    val offset = (serverTime - now).toInt
    println(s"updateClock clientClockOffset: $offset  was: $clientClockOffset")
    clientClockOffset = offset
  }

  /** @return the client estimate of the current server game time */
  def serverGameTime: Long = System.currentTimeMillis() + clientClockOffset

  private def requestGameTime(): Unit = {
    sendMessage(RequestGameTime(System.currentTimeMillis()))
  }

}
