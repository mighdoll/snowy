package snowy.load

import scala.concurrent.Future
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws._
import akka.stream.scaladsl._
import akka.stream.testkit.TestSubscriber.Probe
import akka.stream.testkit.scaladsl.TestSink
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.util.ByteString
import boopickle.Default._
import snowy.GameClientProtocol.GameClientMessage
import snowy.GameServerProtocol.GameServerMessage
import snowy.util.FutureAwaiting._
import socketserve.WebServer.socketApplication
import upickle.default._
import snowy.playfield.Picklers._
import snowy.server.GameControl

object SnowyServerFixture {
  implicit val system = ActorSystem()

  import system.dispatcher

  var testPort = 9100 // incremented with each test, so each test gets its own unique port

  /** API available for server integration tests */
  case class ServerTestApi(sendQueue: SourceQueueWithComplete[GameServerMessage],
                           probe: Probe[GameClientMessage]) {

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
                         timeout: FiniteDuration = 500 milliseconds): A = {
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
                 timeout: FiniteDuration = 3 seconds): Unit = {
    testPort = testPort + 1
    val server = socketApplication(new GameControl(_), Some(testPort))
    try {
      connectToServer(s"ws://localhost:${server.port}/game").flatMap { api =>
        fn(api).flatMap { _ =>
          api.sendQueue.complete()
          api.sendQueue.watchCompletion()
        }
      }.await(timeout)
    } finally {
      Http().shutdownAllConnectionPools() andThen { case _ => server.shutDown() }
    }
  }

  /** Connect to the snowy server at the provided address
    *
    * @param wsUrl
    * @return a test api to send/receive messages against the snowy server
    */
  def connectToServer[M](wsUrl: String): Future[ServerTestApi] = {
    connectSinkToServer(wsUrl, TestSink.probe[GameClientMessage]).map {
      case ((sendQueue, testProbe)) =>
        ServerTestApi(sendQueue, testProbe)
    }
  }

  def connectSinkToServer[M](wsUrl: String, sink: Sink[GameClientMessage, M])
    : Future[(SourceQueueWithComplete[GameServerMessage], M)] = {
    implicit val _       = ActorMaterializer()
    val outputBufferSize = 100

    val sinkFromServer = {
      val messageToGameMessage = Flow[Message].collect {
        case TextMessage.Strict(msg) => read[GameClientMessage](msg)
        case BinaryMessage.Strict(msg) =>
          Unpickle[GameClientMessage].fromBytes(msg.asByteBuffer)
      }
      messageToGameMessage.toMat(sink)(Keep.right)
    }

    val sourceToServer = {
      val sourceQueue =
        Source.queue[GameServerMessage](outputBufferSize, OverflowStrategy.dropTail)
      val gameMessageToBinaryMessage = Flow[GameServerMessage].map { msg =>
        val msgString = Pickle.intoBytes[GameServerMessage](msg)
        BinaryMessage(ByteString(msgString)): Message
      }
      sourceQueue.via(gameMessageToBinaryMessage)
    }

    val flow = Flow.fromSinkAndSourceMat(sinkFromServer, sourceToServer)(Keep.both)

    val (upgradeResponse, (materializedSink, sendQueue)) =
      Http().singleWebSocketRequest(WebSocketRequest(wsUrl), flow)

    upgradeResponse.map { _ =>
      (sendQueue, materializedSink)
    }
  }
}
