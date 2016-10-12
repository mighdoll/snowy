package snowy.server

import socketserve.WebServer.socketApplication

object ServerMain {
  def main(args: Array[String]): Unit = {
    socketApplication(new GameControl(_))
  }
}
