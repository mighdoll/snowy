import boopickle.Default._
import org.scalatest._
import org.scalatest.prop._
import snowy.GameClientProtocol._
import snowy.playfield.Picklers._
import snowy.playfield.PlayId.{BallId, SledId}
import snowy.playfield.{GunnerSled, Sled, Snowball}
import vector.Vec2d

class TestBooPickle extends PropSpec with PropertyChecks {
  val sled = Sled.dummy

  val ball = Snowball(
    ownerId = new SledId(1),
    _position = Vec2d.zero,
    radius = 1,
    mass =   .1,
    speed = Vec2d.zero,
    spawned = 0,
    impactDamage = 0,
    lifetime = 10,
    health = 1
  )

  def pickleUnpickle[T: Pickler](value: T): Unit = {
    val bytes     = Pickle.intoBytes[T](value)
    val unpickled = Unpickle[T](implicitly[Pickler[T]]).fromBytes(bytes)
    assert(unpickled === value)
  }

  property("pickle Died") {
    pickleUnpickle(Died)
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
      State(1L, sleds = Seq(sled), snowballs = Seq(ball))
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
    val state      = State(1L, sleds = Seq(sled), snowballs = Seq(ball))
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
