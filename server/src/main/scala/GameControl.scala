import scala.concurrent.duration._
import GameClientProtocol._
import GameServerProtocol._
import Vec2dClientPosition._
import socketserve.{AppController, AppHostApi, ConnectionId}
import upickle.default._
import GameConstants.Friction.slowButtonFriction
import GameConstants.pushForce

class GameControl(api: AppHostApi) extends AppController with GameState with GameMotion {
  val turnDelta = Math.PI / 30
  val downhillRotation = math.Pi // point up on the screen, towards smaller Y values

  api.tick(20 milliseconds) {
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
    users -= id
    commands.commands -= id
  }

  /** received a client message */
  def message(id: ConnectionId, msg: String): Unit = {
    read[GameServerMessage](msg) match {
      case TurnLeft           => turn(id, turnDelta)
      case TurnRight          => turn(id, -turnDelta)
      case Join(name)         => userJoin(id, name)
      case TurretAngle(angle) => rotateTurret(id, angle)
      case Start(cmd)         => commands.startCommand(id, cmd)
      case Stop(cmd)          => commands.stopCommand(id, cmd)
    }
  }

  private def newRandomSled(): SledState = {
    // TODO what if sled is initialized atop a tree?
    SledState(randomSpot(), size = 30, speed = Vec2d(0, 0),
      rotation = downhillRotation, turretRotation = downhillRotation)
  }

  /** Called when a user sends her name and starts in the game */
  private def userJoin(id: ConnectionId, userName: String): Unit = {
    users(id) = User(userName)
  }

  /** Rotate the turret on a sled */
  private def rotateTurret(id: ConnectionId, angle: Double): Unit = {
    sleds.get(id) match {
      case Some(sled) =>
        sleds(id) = sled.copy(turretRotation = angle)
      case None       =>
        println(s"where's the sled to rotate turret for id: $id")
    }
  }

  /** Rotate a sled.
    *
    * @param rotate rotation in radians from current position. */
  private def turn(id: ConnectionId, rotate: Double): Unit = {
    // TODO limit turn rate to e.g. 1 turn / 50msec to prevent cheating by custom clients?
    val max = math.Pi * 2
    val min = -math.Pi * 2
    sleds.get(id) match {
      case Some(sled) =>
        val rotation = sled.rotation + rotate
        val wrappedRotation =
          if (rotation > max) rotation - max
          else if (rotation < min) rotation - min
          else rotation
        sleds(id) = sled.copy(rotation = wrappedRotation)
      case None       =>
        println(s"where's the sled to turn for connection id: $id")
    }
  }

  /** apply any pending but not yet canelled commands from user actions,
    * e.g. turning or slowing */
  def applyCommands(deltaSeconds:Double): Unit = {
    commands.removeExpired()
    val slow = new InlineForce(-slowButtonFriction * deltaSeconds)
    val push = new InlineForce(pushForce * deltaSeconds)
    commands.foreachCommand { (id, command) =>
      sleds.get(id).map { sled =>
        command match {
          case Left  => turn(id, turnDelta)
          case Right => turn(id, -turnDelta)
          case Slow => sleds(id) = sled.copy(speed = slow(sled.speed))
          case Push => sleds(id) = sled.copy(speed = push(sled.speed))
        }
      }
    }

  }

  /** Called to update game state on a regular timer */
  private def gameTurn(): Unit = {
    val deltaSeconds = nextTimeSlice()
    applyCommands(deltaSeconds)
    moveStuff(deltaSeconds)
    currentState() foreach {
      case (id, state) => api.send(write(state), id)
    }
  }

  /** Advance to the next game simulation state
    *
    * @return the time since the last time slice, in seconds
    */
  private def nextTimeSlice(): Double = {
    val currentTime = System.currentTimeMillis()
    val deltaSeconds = (currentTime - lastTime) / 1000.0
    lastTime = currentTime
    deltaSeconds
  }


}
