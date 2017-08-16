package snowy.server

import scala.concurrent.duration._
import com.typesafe.scalalogging.StrictLogging
import org.scalatest.PropSpec
import snowy.GameConstants.Points
import snowy.GameServerProtocol.{Shooting, Start}
import snowy.server.GameFixture.{InsertedSled, withActorSystem, withGameControl}

class TestKing extends PropSpec with StrictLogging {

  property("become king") {
    withActorSystem { implicit system =>
      withGameControl {
        case game @ GameFixture(gameControl, _) =>
          def king = s"${gameControl.gameTurns.currentKing}"

          val insertSleds              = game.sledInserter()
          val InsertedSled(id0, sled0) = insertSleds()
          game.tickForward(0 milliseconds) // tick, to pick a king

          assert(gameControl.gameTurns.currentKing === Some(sled0))
          val kingScore = gameControl.gameTurns.currentKing.get.user.score

          val InsertedSled(id1, sled1) = insertSleds()
          game.aimAtSled(sled1, sled0, id1)
          gameControl.handleMessage(id1, Start(Shooting, 0))
          game.foreachTick(3000 milliseconds) {
            game.aimAtSled(sled1, sled0, id1)
          }
          assert(gameControl.gameTurns.currentKing === Some(sled1))
          val expectedScore = Points.minPoints + Points.kingBonus +
            (kingScore * Points.sledKill)
          assert(gameControl.gameTurns.currentKing.get.user.score === expectedScore)
      }
    }
  }

}
