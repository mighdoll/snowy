package snowy.server

import scala.concurrent.duration._
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import org.scalatest.PropSpec
import snowy.GameConstants.Points
import snowy.GameServerProtocol.{Shooting, Start}
import snowy.server.GameFixture.{withActorSystem, withGameControl, InsertedSled}

class TestKing extends PropSpec with Logging {

  property("become king") {
    withActorSystem { implicit system =>
      withGameControl {
        case game @ GameFixture(gameControl, _) =>
          def king = s"${gameControl.playfieldSteps.currentKing}"

          val insertSleds              = game.sledInserter()
          val InsertedSled(id0, sled0) = insertSleds()
          game.tickForward(0 milliseconds) // tick, to pick a king

          assert(gameControl.playfieldSteps.currentKing === Some(sled0))
          val kingScore = gameControl.playfieldSteps.currentKing.get.user.score

          val InsertedSled(id1, sled1) = insertSleds()
          game.aimAtSled(sled1, sled0, id1)
          gameControl.handleMessage(id1, Start(Shooting, 0))
          game.foreachTick(3000 milliseconds) {
            game.aimAtSled(sled1, sled0, id1)
          }
          assert(gameControl.playfieldSteps.currentKing === Some(sled1))
          val expectedScore = Points.minPoints + (kingScore * Points.sledKill)
          assert(gameControl.playfieldSteps.currentKing.get.user.score === expectedScore)
      }
    }
  }

}
