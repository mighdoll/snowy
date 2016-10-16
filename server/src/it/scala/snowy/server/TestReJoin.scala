package snowy.server

import org.scalatest._
import org.scalatest.prop._
import SnowyServerFixture.withServer
import upickle.default._
import snowy.GameServerProtocol.{Join, ReJoin, TestDie}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import akka.NotUsed
import akka.http.scaladsl.model.ws.TextMessage
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.testkit.TestSubscriber.Probe
import akka.stream.testkit.scaladsl.TestSink
import snowy.GameClientProtocol.{Died, GameClientMessage, Scoreboard, State}
import scala.concurrent.duration._

class TestReJoin extends PropSpec with PropertyChecks {
  property("test ReJoin") {
    withServer { testApi =>
      import testApi.{send, skipToMessage}

      send(Join("testUser"))
      val origSled = skipToMessage { case State(mySled, _, _) => mySled }
      send(TestDie)
      skipToMessage { case Died => }
      send(ReJoin)
      val newSled = skipToMessage { case State(mySled, _, _) => mySled }
      newSled.id !== origSled.id

      Future.successful(Unit)
    }
  }
}


