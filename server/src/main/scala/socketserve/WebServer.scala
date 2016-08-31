package socketserve

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

/** A web server that hosts static files from the web/ resource directory,
  * scala js output files from the root resource directory,
  * and a websocket for -connect json messages.
  */
class WebServer(implicit system: ActorSystem) {
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val appHost = new AppHost
  val socketFlow = new SocketFlow(appHost)

  val scalaJsFile =
    (""".*?-fastopt\.js$""" +
      """|.*?-fastopt\.js.map$""" +
      """|.*?-jsdeps\.js$""" +
      """|.*?-opt\.js$""" +
      """|.*?-launcher\.js$""").r

  val route =
    pathEndOrSingleSlash {
      getFromResource("web/index.html")
    } ~
      path("game") {
        get {
          handleWebSocketMessages(socketFlow.messages())
        }
      } ~
      path(scalaJsFile) { file =>
        getFromResource(file)
      } ~
      pathSuffix(Segment) { file =>
        getFromResource(s"web/$file")
      }

  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 2345)
  println(s"Server online at http://localhost:2345/")

  def shutDown(): Unit = {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}

object WebServer {
  /** create a web server hosting the given websocket app controller */
  def socketApplication(makeController: AppHostApi => AppController): Unit = {
    implicit val system = ActorSystem()
    val server = new WebServer
    val appHost = server.appHost
    val controller = makeController(appHost)
    appHost.registerApp(controller)
  }
}

