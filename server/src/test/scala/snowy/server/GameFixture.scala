package snowy.server

import scala.concurrent.duration.FiniteDuration
import akka.actor.ActorSystem
import akka.util.ByteString
import com.typesafe.config.ConfigValueFactory
import snowy.GameServerProtocol.{Shooting, Slowing, Start, TargetAngle}
import snowy.measures.{NullMeasurementRecorder, Span}
import snowy.playfield.Sled
import snowy.server.GameFixture.InsertedSled
import socketserve.{AppHostApi, ConnectionId, RobotId, WebServer}
import vector.Vec2d

/** A fixture for running tests against a GameControl */
object GameFixture {

  /** Run a test function with a new actor system that's destroyed
    * when the function completes  */
  def withActorSystem(fn: ActorSystem => Unit): Unit = {
    val system = ActorSystem()
    try {
      fn(system)
    } finally {
      system.terminate()
    }
  }

  /** Run a test function with a GameControl configured for testing. */
  def withGameControl[T](
        fn: GameFixture => T
  )(implicit system: ActorSystem): T = {
    val span = Span.root("game-fixture")(NullMeasurementRecorder)
    val config =
      GlobalConfig.snowy
        .withValue("robot-sleds", ConfigValueFactory.fromAnyRef(0))
        .withValue("server.port", ConfigValueFactory.fromAnyRef(8181))
        .withValue("seed-trees", ConfigValueFactory.fromAnyRef(false))

    val clock       = new ManualClock
    val control     = new GameControl(NullHostApi, system, span, config, clock)
    val gameFixture = GameFixture(control, clock)

    fn(gameFixture)
  }

  // NYI - a test game control that serves the playfield to a web client
  def withGameServer(fn: GameFixture => Unit) {
    WebServer.socketApplication { (api, system, appSpan) =>
      implicit val _system = system
      withGameControl { gameFixture =>
        fn(gameFixture)
        gameFixture.gameControl
      }(system)
    }
  }

  case class InsertedSled(robotId: RobotId, sled: ServerSled)
}

/** Special methods on to enable tests to control the game. */
case class GameFixture(val gameControl: GameControl, clock: ManualClock) {

  /** Aim a sled at another sled */
  def aimAtSled(sled: ServerSled, targetSled: ServerSled, id: RobotId): Unit = {
    val toTarget = targetSled.sled.position - sled.sled.position
    val angle    = Vec2d.unitUp.angle(toTarget)
    gameControl.handleMessage(id, TargetAngle(angle))
  }

  /** Aim a sled to the left and start shooting */
  def shootLeft(id: RobotId): Unit = {
    gameControl.handleMessage(id, TargetAngle(math.Pi / 2))
    gameControl.handleMessage(id, Start(Shooting, 0))
  }

  /** move the game clock forward, and run the game's step function at least once. */
  def tickForward(forward: FiniteDuration): Unit = {
    foreachTick(forward) { () =>
      Unit
    }
  }

  /** Move the game clock forward, and run the game's step function at least once.
    * Call a side effecting function after each game step */
  def foreachTick(forward: FiniteDuration)(
        fn: => Unit
  ): Unit = {
    val period = gameControl.turnPeriod.toMillis
    val ticks  = math.ceil(forward.toMillis.toDouble / period).toInt

    (0 to ticks).foreach { _ =>
      clock.advanceTime(period)
      gameControl.tick()
      fn
    }
  }

  /** debug utility to print sled and snowball locations */
  def printPlayfieldPositions(): Unit = {
    println("playfield:")
    for { sled <- gameControl.serverSleds } {
      println(s"  $sled ${sled.sled.position}")
    }
    for { snowball <- gameControl.snowballs.items } {
      println(s"  $snowball ${snowball.position}")
    }
  }

  /** @return a function that will add a new user and sled into the playfield */
  def sledInserter(): () => InsertedSled = {
    val origin = Vec2d(1000, 1000)
    var index  = 0
    () =>
      val id              = new RobotId()
      val name            = s"sled$index"
      val gapBetweenSleds = 5
      val positionX       = index * (Sled.basicRadius * 2 + gapBetweenSleds)
      val position        = origin + Vec2d(positionX, 0)
      val sled            = gameControl.testUserJoin(id, name, position)
      gameControl.handleMessage(id, Start(Slowing, 0))
      index += 1
      InsertedSled(id, sled)
  }

}

object NullHostApi extends AppHostApi {
  override def sendAll(msg: String): Unit = {}

  override def send(msg: String, id: ConnectionId): Unit = {}

  override def sendBinary(msg: ByteString, id: ConnectionId): Unit = {}
}
