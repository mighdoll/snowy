import java.util.concurrent.ThreadLocalRandom
import scala.collection.mutable
import GameClientProtocol._
import GameServerProtocol._
import socketserve.{AppHostApi, ConnectionId, AppController}
import upickle.default._
import Vec2dClientPosition._
import scala.concurrent.duration._

class GameControl(api: AppHostApi) extends AppController {

  /** A collidable object on the playfield */
  trait PlayfieldObject {
    def size: Double

    def pos: Vec2d
  }

  /* rotation in radians, 0 is down.
   * speed in pixels / second
   */
  case class SledState(pos: Vec2d, size: Double, speed: Vec2d,
                       rotation: Double, turretRotation: Double) extends PlayfieldObject

  case class TreeState(pos: Vec2d, size: Double) extends PlayfieldObject

  case class SnowballState(pos: Vec2d, size: Double, speed: Vec2d) extends PlayfieldObject

  val playField = PlayField(1000, 800)
  var sleds = mutable.Map[ConnectionId, SledState]()
  val trees = randomTrees()
  val snowballs = mutable.ListBuffer[SnowballState]()
  val users = mutable.Map[ConnectionId, User]()
  var lastTime = System.currentTimeMillis()
  val turnDelta = Math.PI / 20
  val maxSpeed = 350

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

  /** pick a random spot on the playfield */
  private def randomSpot(): Vec2d = {
    val random = ThreadLocalRandom.current
    Vec2d(
      random.nextInt(playField.width),
      random.nextInt(playField.height)
    )
  }

  private def newRandomSled(): SledState = {
    // TODO what if sled is initialized atop a tree?
    SledState(randomSpot(), size = 30, speed = Vec2d(0, 0), rotation = 0, turretRotation = 0)
  }

  def gone(id: ConnectionId): Unit = {
    sleds -= id
  }

  def userJoin(id: ConnectionId, userName: String): Unit = {
    users(id) = User(userName)
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

  private def rotateTurret(id: ConnectionId, pos: Position): Unit = {
    sleds.get(id) match {
      case Some(sled) =>
        sleds(id) = sled.copy(turretRotation = -Math.atan2(pos.x - sled.pos.x, pos.y - sled.pos.y))
      case None       =>
        println(s"where's the sled to rotate turret for id: $id")
    }
  }

  /** Apply a user initiated rotation to a single sled */
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

  /** Increase the speed of sleds due to gravity */
  private def applyGravity(deltaSeconds: Double): Unit = {
    val gravity = 100.0
    val gravityFactor = gravity * deltaSeconds
    mapSleds { sled =>
      val delta = Vec2d(
        x = 0,
        y = math.cos(sled.rotation) * gravityFactor
      )
      val newSpeed = (sled.speed + delta).min(maxSpeed)
      sled.copy(speed = newSpeed)
    }
  }

  /** Adjust the speed based on the current rotation */
  private def skidToRotate(deltaSeconds: Double): Unit = {
    val skidSpeed = .75 // seconds to complete a skid
    val skidFactor = math.min(1.0, deltaSeconds * skidSpeed)
    mapSleds { sled =>
      val speed = sled.speed.length
      val current = sled.speed / speed
      val target = Vec2d(
        x = math.sin(sled.rotation),
        y = math.cos(sled.rotation)
      )
      val skidVector = current + ((target - current) * skidFactor)
      val newSpeed = skidVector * speed
      sled.copy(speed = newSpeed)
    }
  }

  /** Run a function that replaces each sled */
  private def mapSleds(fn: SledState => SledState): Unit = {
    sleds = sleds.map { case (id, sled) =>
      id -> fn(sled)
    }
  }

  private def slowFriction(sled: SledState): SledState = ???

  /** Constrain a value between 0 and a max value.
    * values past one border of the range are wrapped to the other side
    * @return the wrapped value */
  private def wrapBorder(value: Double, max: Double): Double = {
    if (value > max * 2.0)
      max
    else if (value > max)
      max - value
    else if (value < max * -2.0)
      0
    else if (value < 0)
      max - value
    else
      value
  }

  /** constrain a position to be within the playfield */
  private def wrapInPlayfield(pos: Vec2d): Vec2d = {
    Vec2d(
      wrapBorder(pos.x, playField.width),
      wrapBorder(pos.y, playField.height)
    )
  }

  /** move movable objects to their new location for this time period */
  private def moveObjects(deltaSeconds: Double): Unit = {
    mapSleds { sled =>
      val moved = sled.pos + (sled.speed * deltaSeconds)
      val wrapped = wrapInPlayfield(moved)
      sled.copy(pos = wrapped)
    }
  }

  private def checkCollisions(): Unit = {
    // TODO
  }

  /** Advance to the gmae clock to the next time slice
    * @return the time in seconds since the last time slice
    */
  private def nextTimeSlice(): Double = {
    val currentTime = System.currentTimeMillis()
    val deltaSeconds = (currentTime - lastTime) / 1000.0
    lastTime = currentTime
    deltaSeconds
  }

  /** Called to update game state on a regular timer */
  private def gameTurn(): Unit = {
    val deltaSeconds = nextTimeSlice()
    applyGravity(deltaSeconds)
    skidToRotate(deltaSeconds)
    moveObjects(deltaSeconds)
    checkCollisions()
    api.sendAll(write(currentState()))
  }

  /** Initialize a set of playfield obstacles */
  private def randomTrees(): Set[TreeState] = {
    val sparsity = 100000
    val num = playField.width * playField.height / sparsity
    (0 to num).map { i =>
      TreeState(randomSpot(), 20)
    }.toSet
  }

  /** Package the relevant state to communicate to the client */
  private def currentState(): State = {
    val clientSleds = sleds.map { case (id, sledState) =>
      val user = users.getOrElse(id, User("???"))
      Sled(user, sledState.pos.toPosition, sledState.rotation, sledState.turretRotation)
    }.toSeq
    val clientSnowballs = snowballs.map { ball =>
      Snowball(ball.size.toInt, ball.pos.toPosition)
    }
    State(clientSleds, clientSnowballs)
  }
}
