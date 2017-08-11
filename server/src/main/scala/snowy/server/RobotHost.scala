package snowy.server

import scala.collection.mutable
import snowy.GameServerProtocol.GameServerMessage
import snowy.measures.Span.time
import snowy.playfield.PlayId.SledId
import snowy.robot.{Robot, RobotApi, RobotGameState}
import snowy.util.ActorTypes.ParentSpan
import socketserve.{ClientId, RobotId}


/** */
class RobotHost(gameControl: GameControl) {
  private val robots     = mutable.Map[RobotId, Robot]()
  private val robotSleds = mutable.Map[RobotId, SledId]()

  /** create a new robot */
  def createRobot(fn: RobotApi => Robot): Unit = {
    val id    = new RobotId()
    val api   = new OneRobotApi(gameControl, id)
    val robot = fn(api)
    robots(id) = robot
  }

  /** let all the robots update state and send commands */
  def robotsTurn[_:ParentSpan](): Unit = time("robotsTurn") {
    for {
      (connectionId, robot) <- robots
      sledId                <- robotSleds.get(connectionId)
    } {
      val robotGameState = InternalRobotState.gameState(gameControl, sledId)
      robot.refresh(robotGameState)
    }
  }

  def died(id: RobotId): Unit = {
    robots.get(id).foreach(_.killed())
  }

  def joined(connectionId: RobotId, sledId: SledId): Unit = {
    robotSleds(connectionId) = sledId
  }
}

/** api interface provided to one server hosted robot */
class OneRobotApi(gameControl: GameControl, id: ClientId) extends RobotApi {

  override def sendToServer(message: GameServerMessage): Unit = {
    gameControl.handleMessage(id, message)
  }

}

object InternalRobotState {
  def gameState(state: GameState, sledId: SledId): RobotGameState = {
    RobotGameState(
      mySledId = sledId,
      allSleds = state.sleds.items,
      snowballs = state.snowballs.items,
      trees = state.trees.items,
      playfield = state.playfield.size
    )
  }
}
