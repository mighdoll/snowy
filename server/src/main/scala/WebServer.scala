import scala.io.StdIn

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

object WebServer {
  def main(args: Array[String]):Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val protoFlow = new ProtoFlow()

    val route =
      pathPrefixTest("sock-client") {
        pathSuffix(Segment) { file =>
          getFromResource(file)
        }
      } ~
      pathEndOrSingleSlash {
        getFromResource("web/index.html")
      } ~
      path("game") {
        get {
          handleWebSocketMessages(protoFlow.messages())
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 2345)
    println(s"Server online at http://localhost:2345/\nPress RETURN to stop...")
    StdIn.readLine() // run until user presses return
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
