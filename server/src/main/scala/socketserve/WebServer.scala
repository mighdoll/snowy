package socketserve

import scala.util.Properties
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, BindFailedException}
import snowy.util.FutureAwaiting._

/** A web server that hosts static files from the web/ resource directory,
  * scala js output files from the root resource directory,
  * and a websocket for -connect json messages.
  */
class WebServer(forcePort: Option[Int] = None)(implicit system: ActorSystem) {
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val defaultPort               = 9000

  val appHost    = new AppHost
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

  val port =
    forcePort.orElse(Properties.envOrNone("PORT").map(_.toInt)).getOrElse(defaultPort)

  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", port)
  bindingFuture.failed.foreach { failure =>
    println(s"Server unable to start at http://localhost:$port/  $failure")
    failure match {
      case BindFailedException => println(s"is port $port already in use?")
      case _                   =>
    }
    sys.exit(1)
  }
  bindingFuture.await()
  bindingFuture.foreach { _ =>
    println(s"Server online at http://localhost:$port/")
  }

  def shutDown(): Unit = {
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}

object WebServer {

  /** create a web server hosting the given websocket app controller */
  def socketApplication(makeController: AppHostApi => AppController,
                        forcePort: Option[Int] = None): WebServer = {
    implicit val system = ActorSystem()
    val server          = new WebServer(forcePort)
    val appHost         = server.appHost
    val controller      = makeController(appHost)
    appHost.registerApp(controller)
    server
  }
}
