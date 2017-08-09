package snowy.server

import com.typesafe.scalalogging.StrictLogging
import snowy.playfield.Rect
import snowy.robot.DeadRobot
import socketserve.ClientId
import vector.Vec2d

/** Support development only - debugging commands for the game */
class GameDebug(gameState:GameState, robots:RobotHost) extends StrictLogging {
  import gameState._
  import gameState.gameStateImplicits._

  private lazy val clientDebugEnabled = GlobalConfig.snowy.getBoolean("client-debug-messages")

  /** process a debug command sent by the client */
  def debugCommand(id: ClientId, key: Char): Unit = {
    if (clientDebugEnabled) {
      key match {
        case 't' => logNearbyTrees(id)
        case '9' => createStationarySleds(99)
        case x   => logger.warn(s"debug key $x not recognized from client $id")
      }
    }
  }

  private def logNearbyTrees(id: ClientId): Unit = {
    debugVerifyGridState()
    debugVerifyTreeState()

    for { sled <- id.sled } {
      logger.warn(s"logNearbyTrees.sled: $sled  position: ${sled.position}")
      val bounds = sled.boundingBox
      val bigger = Rect(bounds.pos - Vec2d(25, 25), bounds.size + Vec2d(50, 50))
      for { tree <- trees.grid.inside(bigger) } {
        logger.warn(s"logNearbyTrees.tree: $tree  position: ${tree.position}")
      }
    }
  }

  /** Add some dummy sleds to the game */
  private def createStationarySleds(number: Int): Unit = {
    (1 to number).foreach { _ =>
      robots.createRobot(DeadRobot.apply)
    }
  }


}
