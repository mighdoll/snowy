package snowy.client

import network.NetworkSocket
import org.scalajs.dom._
import snowy.connection.{InboundEvents, OutboundEvents}

import scala.concurrent.duration._

class Connection(name: String) {
  val socket = {
    val inDelay = 0 milliseconds
    val outDelay = 0 milliseconds
    val url = s"ws://${window.location.host}/game"
    new NetworkSocket(url, inDelay, outDelay)
  }

  new InboundEvents(socket, name)
  new OutboundEvents(socket)
}
