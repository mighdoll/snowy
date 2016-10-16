package snowy.server

import scala.concurrent.Future
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws._
import akka.stream.scaladsl._
import akka.stream.testkit.TestSubscriber.Probe
import akka.stream.testkit.scaladsl.TestSink
import akka.stream.{ActorMaterializer, OverflowStrategy}
import snowy.GameClientProtocol.GameClientMessage
import snowy.GameServerProtocol.GameServerMessage
import snowy.server.FutureAwaiting._
import socketserve.WebServer.socketApplication
import upickle.default._

object SnowyServerFixture {
  implicit val system = ActorSystem()

  import system.dispatcher

  var testPort = 9100 // incremented with each test, so each test gets its own unique port

  /** API available for server integration tests */
  case class ServerTestApi(sendQueue: SourceQueueWithComplete[GameServerMessage],
                           probe: Probe[GameClientMessage]
                          ) {
    /** send a message to the server */
    def send(message: GameServerMessage): Unit = {
      sendQueue.offer(message)
    }

    /** skip over received messages until the first message matching a
      * provided function is reached. Throws a timeout exception if the
      * message isn't received in time
      * @param pFn partial function to match messages
      * @return result of partial function
      */
    def skipToMessage[A](pFn: PartialFunction[GameClientMessage, A],
                        timeout: FiniteDuration = 100 milliseconds): A = {
      val messages = Iterator.continually(probe.requestNext(timeout))
      messages.collectFirst(pFn).get
    }
  }

  /** Start a new snowy server and run tests against it.
    *
     * @param fn a test function that's provided with a send/receive api
    *           the function should return a future that completes
    *           when the test is done
    */
  def withServer(fn: ServerTestApi => Future[Unit],
                 timeout:FiniteDuration = 3 seconds): Unit = {
    testPort = testPort + 1
    val server = socketApplication(new GameControl(_), Some(testPort))
    try {
      connect(s"ws://localhost:${server.port}/game").flatMap { api =>
        fn(api).flatMap{_ =>
          api.sendQueue.complete()
          api.sendQueue.watchCompletion()
        }
      }.await(timeout)
    } finally {
      Http().shutdownAllConnectionPools() andThen {case _ => server.shutDown()}
    }
  }

  /** Connect to the snowy server at the provided address
    *
    * @param wsUrl
    * @return a test api to send/receive messages against the snowy server
    */
  private def connect(wsUrl: String): Future[ServerTestApi] = {
    implicit val _ = ActorMaterializer()
    val outputBufferSize = 100

    val testSinkFromServer = {
      val messageToString = Flow[Message]
        .collect { case orig@TextMessage.Strict(msg) => read[GameClientMessage](msg) }
      messageToString.toMat(TestSink.probe[GameClientMessage])(Keep.right)
    }

    val sourceToServer = {
      val sourceQueue = Source.queue[GameServerMessage](outputBufferSize, OverflowStrategy.dropTail)
      val gameMessageToTextMessage = Flow[GameServerMessage]
        .map { msg =>
          val msgString = write[GameServerMessage](msg)
          TextMessage(msgString): Message
        }
      sourceQueue.via(gameMessageToTextMessage)
    }

    val flow = Flow.fromSinkAndSourceMat(testSinkFromServer, sourceToServer)(Keep.both)

    val (upgradeResponse, (testProbe, sendQueue)) =
      Http().singleWebSocketRequest(WebSocketRequest(wsUrl), flow)

    upgradeResponse.map { upgradeResponse =>
      ServerTestApi(sendQueue, testProbe)
    }
  }
}
