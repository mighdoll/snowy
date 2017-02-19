package snowy.robot

import snowy.GameServerProtocol.GameServerMessage
import snowy.playfield.PlayId.SledId
import snowy.playfield._
import vector.Vec2d

/** API that the Robot can use */
trait RobotApi {
  def sendToServer(gameServerMessage: GameServerMessage): Unit
}

/** API that the Robot should implement */
trait Robot {

  /** Called when the game state has been updated. The robot should respond
    * with commands
    * TODO: return a Seq[GameServerMessage] */
  def refresh(state: RobotGameState): Unit

  /** called when the robot sled is killed in the game */
  def killed(): Unit
}

/** Raw information delivered to the robot */
trait RobotGameStateInfo {

  /** Id of this robots sled.
    * The id may change if the robot is killed and rejoins.  */
  def mySledId: SledId

  /** collection of all sleds in the game, including the robot's sled */
  def sleds: Traversable[Sled]

  /** collection of all snowballs in the game, including the ones thrown by this robot */
  def snowballs: Traversable[Snowball]

  /** collection of all trees in the game */
  def trees: Traversable[Tree]

  /** size of the playfield in pixels */
  def playfield: Vec2d

}

/** gaame state information packaged conveniently for the robot */
case class RobotGameState(mySledId: SledId,
                          allSleds: Traversable[Sled],
                          snowballs: Traversable[Snowball],
                          trees: Traversable[Tree],
                          playfield: Vec2d) {
  lazy val (mySled: Option[Sled], otherSleds: Traversable[Sled]) = {
    val (mine, otherSleds) = allSleds.partition(_.id == mySledId)
    (mine.headOption, otherSleds)
  }
}

object RobotGameState {
  val emptyGameState = RobotGameState(new SledId(-1), Set(), Set(), Set(), Vec2d.zero)
}
