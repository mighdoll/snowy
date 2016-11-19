package snowy.robot

import snowy.GameServerProtocol.GameServerMessage
import snowy.playfield._
import vector.Vec2d

/** API that the Robot can use */
trait RobotApi {
  def sendToServer(gameServerMessage: GameServerMessage)
  def join(name: String, kind: SledKind, skiColor: SkiColor = BasicSkis): Sled
  def rejoin(): Sled
}

/** API that the Robot should implement */
trait Robot {
  def refresh(state: RobotState): Unit
  def killed(): Unit
}

/** Information delivered to the robot */
trait RobotState {
  def sleds: Traversable[Sled]
  def snowballs: Traversable[Snowball]
  def trees: Traversable[Tree]
  def playfield: Vec2d
}
