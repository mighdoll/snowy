import boopickle.Default._
import org.scalatest._
import org.scalatest.prop._
import snowy.GameClientProtocol._
import snowy.sleds._
import snowy.playfield.Picklers._
import snowy.playfield.PlayId.{BallId, SledId}
import snowy.playfield.{Sled, Snowball}
import vector.Vec2d

class TestBooPickle extends PropSpec with PropertyChecks {
  val sled = Sled(
    id = new SledId(0),
    userName = "fo",
    pos = Vec2d.zero,
    size = 0,
    speed = Vec2d.zero,
    rotation = 0,
    turretRotation = 0,
    kind = GunnerSled
  )

  val ball = Snowball(
    id = new BallId(0),
    ownerId = new SledId(1),
    pos = Vec2d.zero,
    size = 1,
    speed = Vec2d.zero,
    spawned = 0,
    power = 0)

  def pickleUnpickle[T: Pickler](value: T): Unit = {
    val bytes     = Pickle.intoBytes[T](value)
    val unpickled = Unpickle[T](implicitly[Pickler[T]]).fromBytes(bytes)
    unpickled === value
  }

  property("pickle Died") {
    val diedBytes = Pickle.intoBytes(Died)
  }
  property("pickle Vec2d") {
    pickleUnpickle(Vec2d.unitUp)
  }
  property("pickle BallId") {
    pickleUnpickle(new BallId(1))
  }
  property("pickle snowball") {
    pickleUnpickle(ball)
  }
  property("pickle sled") {
    pickleUnpickle(sled)
  }
  property("pickle state") {
    pickleUnpickle(
      State(sled, sleds = Seq(sled), snowballs = Seq(ball))
    )
  }
  property("pickle scoreboard") {
    pickleUnpickle(
      Scoreboard(1.1, Seq(Score("fred", .8)))
    )
  }

  property("pickle game client message Ping") {
    val bytes  = Pickle.intoBytes[GameClientMessage](Ping)
    val result = Unpickle[GameClientMessage].fromBytes(bytes)
    result === Ping
  }

  property("pickle game client message State and Ping") {
    val state      = State(sled, sleds = Seq(sled), snowballs = Seq(ball))
    val stateBytes = Pickle.intoBytes[GameClientMessage](state)
    val unpickledState: GameClientMessage =
      Unpickle[GameClientMessage].fromBytes(stateBytes)

    val pingBytes = Pickle.intoBytes[GameClientMessage](Ping)
    val unpickledPing: GameClientMessage =
      Unpickle[GameClientMessage].fromBytes(pingBytes)

    unpickledState match {
      case s: State =>
      case _        => fail
    }

    unpickledPing match {
      case Ping =>
      case _    => fail
    }
  }

}
