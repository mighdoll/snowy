package snowy.server

import org.scalatest._
import org.scalatest.prop._
import snowy.GameClientProtocol.{Died, MySled}
import snowy.GameServerProtocol.{Join, ReJoin, TestDie}
import snowy.load.SnowyServerFixture.withServer

import scala.concurrent.Future

class TestReJoin extends PropSpec with PropertyChecks {
  property("ReJoin registers user with a new sled") {
    withServer { testApi =>
      import testApi.{send, skipToMessage}

      send(Join("testUser"))
      val origSled = skipToMessage { case MySled(mySled) => mySled }
      send(TestDie)
      skipToMessage { case Died => }
      send(ReJoin)
      val newSled = skipToMessage { case MySled(mySled) => mySled }
      newSled.id !== origSled.id

      Future.successful(Unit)
    }
  }
}
