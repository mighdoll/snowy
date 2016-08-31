import scala.concurrent.duration._
import GameClientProtocol._
import GameServerProtocol._
import Vec2dClientPosition._
import socketserve.{AppController, AppHostApi, ConnectionId}
import upickle.default._

class GameControl(api: AppHostApi) extends AppController with GameState with GameMotion {

  api.tick(50 milliseconds) {
    gameTurn()
  }

  /** a new player has connected */
  def open(id: ConnectionId): Unit = {
    api.send(write(playField), id)
    val clientTrees = trees.map { treeState =>
      Tree(treeState.size.toInt, treeState.pos.toPosition)
    }.toSeq
    api.send(write(Trees(clientTrees)), id)
    sleds += id -> newRandomSled()
  }

  /** Called when a connection is dropped */
  def gone(id: ConnectionId): Unit = {
    sleds -= id
  }

  /** received a client message */
  def message(id: ConnectionId, msg: String): Unit = {
    read[GameServerMessage](msg) match {
      case TurnLeft   => turn(id, -turnDelta)
      case TurnRight  => turn(id, turnDelta)
      case Join(name) => userJoin(id, name)
      case Mouse(pos) => rotateTurret(id, pos)
      case x          => println(s"received unhandled client message: $x")
    }
  }

  private def newRandomSled(): SledState = {
    // TODO what if sled is initialized atop a tree?
    SledState(randomSpot(), size = 30, speed = Vec2d(0, 0), rotation = 0, turretRotation = 0)
  }

  /** Called when a user sends her name and starts in the game */
  private def userJoin(id: ConnectionId, userName: String): Unit = {
    users(id) = User(userName)
  }

  /** Rotate the turret on a sled */
  private def rotateTurret(id: ConnectionId, pos: Position): Unit = {
    sleds.get(id) match {
      case Some(sled) =>
        sleds(id) = sled.copy(turretRotation = -Math.atan2(pos.x - sled.pos.x, pos.y - sled.pos.y))
      case None       =>
        println(s"where's the sled to rotate turret for id: $id")
    }
  }

  /** Rotate a sled */
  private def turn(id: ConnectionId, direction: Double): Unit = {
    // TODO limit turn rate to e.g. 1 turn / 50msec
    val maxRight = math.Pi / 2
    val maxLeft = -math.Pi / 2
    sleds.get(id) match {
      case Some(sled) =>
        val baseRotation = sled.rotation + direction
        val rotation =
          if (baseRotation > maxRight) maxRight
          else if (baseRotation < maxLeft) maxLeft
          else baseRotation

        sleds(id) = sled.copy(rotation = rotation)
      case None       =>
        println(s"where's the sled to turn for connection id: $id")
    }
  }

  /** Called to update game state on a regular timer */
  private def gameTurn(): Unit = {
    moveStuff(nextTimeSlice())
    api.sendAll(write(currentState()))
  }

  /** Advance to the next game simulation state
    *
    * @return the time in seconds since the last time slice
    */
  private def nextTimeSlice(): Double = {
    val currentTime = System.currentTimeMillis()
    val deltaSeconds = (currentTime - lastTime) / 1000.0
    lastTime = currentTime
    deltaSeconds
  }


}
