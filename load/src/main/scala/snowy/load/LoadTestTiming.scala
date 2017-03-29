package snowy.load

import scala.concurrent.ExecutionContext
import akka.stream.scaladsl.Sink
import snowy.GameClientProtocol.GameClientMessage
import snowy.GameServerProtocol.ClientPing
import snowy.load.LoadTestTiming.EC
import snowy.load.SnowyServerFixture.connectSinkToServer

object LoadTestTiming {
  type EC = ExecutionContext
}

class LoadTestTiming[E: EC](url: String) {

  val sink = Sink.foreach[GameClientMessage] {
    case _ =>
  }

//  val sink: Sink[GameClientMessage, _] = flow.to(Sink.ignore)

  for ((sendQ, _)  <- connectSinkToServer(url, sink)) {
    sendQ.offer(ClientPing)
  }
}
