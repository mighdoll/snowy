package snowy.server

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import snowy.GameConstants
import snowy.GameServerProtocol.GameServerMessage
import snowy.playfield._
import snowy.robot.{Robot, RobotApi, RobotState}
import socketserve.ConnectionId
import vector.Vec2d

class RobotHost(gameControl: GameControl) {
  private val robots = mutable.Map[ConnectionId,Robot]()

  /** create a new robot */
  def createRobot(fn: RobotApi => Robot): Unit = {
    val id    = new ConnectionId()
    val api   = new OneRobotApi(gameControl, id)
    val robot = fn(api)
    robots(id) = robot
  }

  /** let all the robots update state and send commands */
  def robotsTurn(): Unit = {
    val state = new InternalRobotState(gameControl)
    robots.values.foreach { robot =>
      robot.refresh(state)
    }
  }

  def died(id: ConnectionId): Unit = {
    robots.get(id).foreach(_.killed())
  }

}

/** api interface provided to one server robot */
class OneRobotApi(gameControl: GameControl, id: ConnectionId) extends RobotApi {

  override def sendToServer(message: GameServerMessage): Unit = {
    gameControl.handleMessage(id, message)
  }

  override def join(name: String, kind: SledKind, skiColor: SkiColor): Sled = {
    gameControl.userJoin(id, name, kind, skiColor, robot = true)
  }

  override def rejoin(): Sled = {
    gameControl.rejoin(id).getOrElse(Sled.dummy)
  }
}

class InternalRobotState(state: GameState) extends RobotState {
  override def sleds            = state.sleds.items
  override def snowballs        = state.snowballs.items
  override def trees            = state.trees
  override def playfield: Vec2d = GameConstants.playfield
}
