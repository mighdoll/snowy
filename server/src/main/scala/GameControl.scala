import scala.concurrent.duration._
import GameClientProtocol._
import GameServerProtocol._
import Vec2dClientPosition._
import socketserve.{AppController, AppHostApi, ConnectionId}
import upickle.default._
import GameConstants.Friction.slowButtonFriction
import GameConstants._
import math.min

class GameControl(api: AppHostApi) extends AppController with GameState with GameMotion {
  val tickDelta = 20 milliseconds
  val turnDelta = (math.Pi / turnTime) * (tickDelta.toMillis / 1000.0)

  api.tick(tickDelta) {
    gameTurn()
  }

  /** a new player has connected */
  override def open(id: ConnectionId): Unit = {
    api.send(write(playField), id)
    val clientTrees = trees.map { treeState =>
      Tree(treeState.size.toInt, treeState.pos.toPosition)
    }.toSeq
    api.send(write(Trees(clientTrees)), id)
    sleds.add(id, newRandomSled())
  }

  /** Called when a connection is dropped */
  override def gone(id: ConnectionId): Unit = {
    sleds.remove(id)
    users.remove(id)
    commands.commands.remove(id)
  }

  /** received a client message */
  override def message(id: ConnectionId, msg: String): Unit = {
    read[GameServerMessage](msg) match {
      case Join(name)         => userJoin(id, name)
      case TurretAngle(angle) => rotateTurret(id, angle)
      case Shoot              => createSnowball(id)
      case Start(cmd)         => commands.startCommand(id, cmd)
      case Stop(cmd)          => commands.stopCommand(id, cmd)
    }
  }

  private def newRandomSled(): SledState = {
    // TODO what if sled is initialized atop a tree?
    SledState(randomSpot(), size = 35, speed = Vec2d(0, 0),
      rotation = downhillRotation, turretRotation = downhillRotation)
  }

  /** Called when a user sends her name and starts in the game */
  private def userJoin(id: ConnectionId, userName: String): Unit = {
    users(id) = User(userName)
  }

  /** Rotate the turret on a sled */
  private def rotateTurret(id: ConnectionId, angle: Double): Unit = {
    sleds.modify(id)(_.copy(turretRotation = angle))
  }

  private def createSnowball(id: ConnectionId): Unit = {
    sleds.forOneSled(id) {sled =>
      val direction = Vec2d.fromRotation(-sled.turretRotation)
      snowballs += SnowballState(
        sled.pos + direction * 35, 10,
        (sled.speed / 50) + (direction * 10),
        System.currentTimeMillis())
    }
  }

  /** Rotate a sled.
    *
    * @param rotate rotation in radians from current position. */
  private def turnSled(sled:SledState, rotate: Double): SledState = {
    // TODO limit turn rate to e.g. 1 turn / 50msec to prevent cheating by custom clients?
    val max = math.Pi * 2
    val min = -math.Pi * 2
    val rotation = sled.rotation + rotate
    val wrappedRotation =
      if (rotation > max) rotation - max
      else if (rotation < min) rotation - min
      else rotation
    sled.copy(rotation = wrappedRotation)
  }

  /** apply any pending but not yet cancelled commands from user actions,
    * e.g. turning or slowing */
  def applyCommands(deltaSeconds: Double): Unit = {
    commands.removeExpired()
    val slow = new InlineForce(-slowButtonFriction * deltaSeconds)
    val pushForceNow = PushEnergy.force * deltaSeconds
    val pushEffort = deltaSeconds / PushEnergy.maxTime
    val push = new InlineForce(pushForceNow)
    commands.foreachCommand { (id, command) =>
      sleds.modify(id) { sled =>
        command match {
          case Left  => turnSled(sled, turnDelta)
          case Right => turnSled(sled, -turnDelta)
          case Slow  => sled.copy(speed = slow(sled.speed))
          case Push  => pushSled(sled, pushForceNow, push, pushEffort)
        }
      }
    }
  }

  /** apply a push to a sled */
  private def pushSled(sled: SledState, pushForceNow: Double, push: InlineForce, effort: Double)
  : SledState = {
    if (effort < sled.pushEnergy) {
      val speed =
        if (sled.speed.zero) Vec2d.fromRotation(sled.rotation) * pushForceNow
        else push(sled.speed)
      val energy = sled.pushEnergy - effort
      sled.copy(speed = speed, pushEnergy = energy)
    } else {
      sled
    }
  }

  /** slowly recover some health points */
  private def recoverHealth(deltaSeconds: Double): Unit = {
    val deltaHealth = deltaSeconds / Health.recoveryTime
    sleds.mapSleds { sled =>
      val newHealth = min(1.0, sled.health + deltaHealth)
      sled.copy(health = newHealth)
    }
  }

  /** slowly recover some push energy */
  private def recoverPushEnergy(deltaSeconds: Double): Unit = {
    val deltaEnergy = deltaSeconds / PushEnergy.recoveryTime
    sleds.mapSleds { sled =>
      val energy = min(1.0, sled.pushEnergy + deltaEnergy)
      sled.copy(pushEnergy = energy)
    }
  }


  /** Called to update game state on a regular timer */
  private def gameTurn(): Unit = {
    val deltaSeconds = nextTimeSlice()
    recoverHealth(deltaSeconds)
    recoverPushEnergy(deltaSeconds)
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
