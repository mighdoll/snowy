import java.util.concurrent.ThreadLocalRandom
import scala.collection.mutable
import GameClientProtocol._
import GameServerProtocol._
import socketserve.{AppHostApi, ConnectionId, AppController}
import upickle.default._
import Vec2dClientPosition._
import scala.concurrent.duration._

class GameControl(api:AppHostApi) extends AppController {

  /** A collidable object on the playfield */
  trait PlayfieldObject {
    def size:Double
    def pos:Vec2d
  }

  /* rotation in radians, 0 is down.
   * speed in pixels / second
   */
  case class SledState(pos:Vec2d, size:Double, speed:Vec2d,
                       rotation:Double, turretRotation:Double) extends PlayfieldObject
  case class TreeState(pos:Vec2d, size:Double) extends PlayfieldObject
  case class SnowballState(pos:Vec2d, size:Double, speed:Vec2d) extends PlayfieldObject

  val playField = PlayField(500, 500)
  var sleds = mutable.Map[ConnectionId, SledState]()
  val trees = randomTrees()
  val snowballs  = mutable.ListBuffer[SnowballState]()
  val users = mutable.Map[ConnectionId, User]()
  var lastTime = System.currentTimeMillis()
  val turnDelta = Math.PI / 20
  val maxSpeed = 200

  api.tick(50 milliseconds) { gameTurn() }

  /** a new player has connected */
  def open(id:ConnectionId):Unit = {
    api.send(write(playField), id)
    val clientTrees = trees.map { treeState =>
      Tree(treeState.size.toInt, treeState.pos.toPosition)
    }.toSeq
    api.send(write(Trees(clientTrees)), id)
    sleds += id -> newRandomSled()
  }

  /** pick a random spot on the playfield */
  private def randomSpot():Vec2d = {
    val random = ThreadLocalRandom.current
    Vec2d(
      random.nextInt(playField.width),
      random.nextInt(playField.height)
    )
  }

  private def newRandomSled():SledState = {
    // TODO what if sled is initialized atop a tree?
    SledState(randomSpot(), size=30, speed=Vec2d(0,0), rotation=0, turretRotation=0)
  }

  def gone(id:ConnectionId):Unit = {
    sleds -= id
  }

  def userJoin(id:ConnectionId, userName:String):Unit = {
    users(id) = User(userName)
  }

  /** received a client message */
  def message(id:ConnectionId, msg:String):Unit = {
    read[GameServerMessage](msg) match {
      case TurnLeft => turn(id, -turnDelta)
      case TurnRight => turn(id, turnDelta)
      case Join(name) => userJoin(id, name)
      case x => println(s"received unhandled client message: $x")
    }
  }

  private def turn(id:ConnectionId, direction:Double):Unit = {
    // TODO limit turn rate to 1 turn / 50msec
    sleds.get(id) match {
      case Some(sled) =>
        sleds(id) = sled.copy(rotation = sled.rotation + direction)
      case None =>
        println(s"where's the sled to turn for connection id: $id")
    }
  }

  /** increase the speed of sleds due to gravity */
  private def applyGravity():Unit = {
    val gravity = 20.0
    sleds = sleds.map {case (id, sled) =>
      val delta = Vec2d(
        x = Math.sin(sled.rotation) * gravity,
        y = Math.cos(sled.rotation) * gravity
      )
      val newSpeed = (sled.speed + delta).min(maxSpeed)
      id -> sled.copy(speed = newSpeed)
    }
  }

  /** constrain a value between 0 and a max value.
    * values past one border are wrapped out the other side */
  private def wrapBorder(value:Double, max:Double):Double = {
    if (value > max * 2.0)
      max
    else if (value > max)
      max - value
    else if (value < max * - 2.0)
      0
    else if (value < 0)
      max - value
    else
      value
  }

  /** constrain a position to be within the playfield */
  private def wrapInPlayfield(pos:Vec2d):Vec2d = {
    Vec2d(
      wrapBorder(pos.x, playField.width),
      wrapBorder(pos.y, playField.height)
    )
  }

  /** move movable objects to their new location for this time period */
  private def moveObjects():Unit = {
    val currentTime = System.currentTimeMillis()
    val deltaSeconds = (currentTime - lastTime) / 1000.0
    lastTime = currentTime
    sleds = sleds.map { case(id, sled) =>
      val moved = sled.pos + (sled.speed * deltaSeconds)
      val wrapped = wrapInPlayfield(moved)
      id -> sled.copy(pos = wrapped)
    }
  }

  private def checkCollisions():Unit = {
    // TODO
  }

  /** called to update game state on a regular timer */
  private def gameTurn(): Unit = {
    applyGravity()
    moveObjects()
    checkCollisions()
    api.sendAll(write(currentState()))
  }

  /** initialize a set of playfield obstacles */
  private def randomTrees():Set[TreeState] = {
    val sparsity = 100000
    val num = playField.width * playField.height / sparsity
    (0 to num).map{ i =>
      TreeState(randomSpot(), 20)
    }.toSet
  }

  /** package up the state to communicate to the client */
  private def currentState():State = {
    val clientSleds = sleds.map{case (id, sledState) =>
      val user = users.getOrElse(id, User("???"))
      Sled(user, sledState.pos.toPosition, sledState.rotation, sledState.turretRotation)
    }.toSeq
    val clientSnowballs = snowballs.map{ball =>
      Snowball(ball.size.toInt, ball.pos.toPosition)
    }
    State(clientSleds, clientSnowballs)
  }
}
