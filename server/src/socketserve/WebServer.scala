package socketserve

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.BindFailedException
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import snowy.server.GlobalConfig
import snowy.util.FutureAwaiting._
import snowy.util.ActorUtil.materializerWithLogging
import scala.util.Properties
import snowy.measures.{MeasurementRecorder, Span}

/** A web server that hosts static files from the web/ resource directory,
  * scala js output files from the root resource directory,
  * and a WebSocket for -connect json messages.
  */
class WebServer(forcePort: Option[Int] = None)(implicit system: ActorSystem,
                                               parentSpan: Span)
    extends Logging {
  private implicit val materializer     = materializerWithLogging(logger)
  private implicit val executionContext = system.dispatcher

  val appHost    = new AppHost
  val socketFlow = new SocketFlow(appHost)

  val scalaJsFile =
    ("""out\.js$""" +
      """|out\.js\.map""").r

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
      getFromResourceDirectory("web")

  val port = {
    forcePort
      .orElse(Properties.envOrNone("PORT").map(_.toInt))
      .getOrElse(GlobalConfig.config.getInt("snowy.server.port"))
  }

  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", port)
  bindingFuture.failed.foreach { failure =>
    logger.error(s"Server unable to start at http://localhost:$port/  $failure")
    failure match {
      case _: BindFailedException => logger.error(s"is port $port already in use?")
      case _                      =>
    }
    sys.exit(1)
  }
  bindingFuture.await()
  bindingFuture.foreach { _ =>
    logger.info(s"Server online at http://localhost:$port/")
  }

  def shutDown(): Unit = {
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}

object WebServer {

  /** create a web server hosting the given WebSocket app controller */
  def socketApplication(makeController: (AppHostApi, ActorSystem, Span) => AppController,
                        forcePort: Option[Int] = None): WebServer = {
    implicit val system              = ActorSystem()
    implicit val measurementRecorder = MeasurementRecorder(GlobalConfig.config)(system)
    implicit val rootSpan            = Span.root("SocketApplication").finishNow()
    val server                       = new WebServer(forcePort)
    val appHost                      = server.appHost
    val controller                   = makeController(appHost, system, rootSpan)
    appHost.registerApp(controller)
    server
  }
}
