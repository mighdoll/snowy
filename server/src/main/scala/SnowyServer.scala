import akka.actor.ActorSystem
import socketserve.WebServer.socketApplication

object SnowyServer {
  def main(args: Array[String]):Unit = {
    socketApplication(new GameControl(_))
  }
}
