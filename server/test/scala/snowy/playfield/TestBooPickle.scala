package snowy.playfield

import boopickle.DefaultBasic._
import org.scalatest._
import org.scalatest.prop._
import snowy.GameClientProtocol._
import snowy.GameServerProtocol.{GameServerMessage, Join}
import snowy.playfield.Picklers._
import snowy.playfield.PlayId.BallId
import snowy.playfield.SnowballFixture.testSnowball
import vector.Vec2d

class TestBooPickle extends PropSpec with PropertyChecks {
  val sled = {
    Sled.dummy
  }

  val ball = testSnowball()

  def pickleUnpickle[T: Pickler](value: T): T = {
    val bytes     = Pickle.intoBytes[T](value)
    val unpickled = Unpickle[T](implicitly[Pickler[T]]).fromBytes(bytes)
    assert(unpickled === value)
    unpickled
  }

  def compareSleds(a: Sled, b: Sled): Unit = {
    assert(a.id === b.id)
    assert(a.position === b.position)
    assert(a.speed === b.speed)
    assert(a.skiColor === b.skiColor)
    assert(a.rotation === b.rotation)
    assert(a.health === b.health)
    assert(a.radius === b.radius)
    assert(a.rotation === b.rotation)
    assert(a.lastShotTime === b.lastShotTime)
    assert(a.lastBoostTime === b.lastBoostTime)
    assert(a.mass === b.mass)
    assert(a.position === b.position)
    assert(a.maxSpeed === b.maxSpeed)
    assert(a.maxHealth === b.maxHealth)
    assert(a === b)
  }

  def compareSnowballs(a: Snowball, b: Snowball): Unit = {
    assert(a.id === b.id)
    assert(a.ownerId === b.ownerId)
    assert(a.position === b.position)
    assert(a.speed === b.speed)
    assert(a.radius === b.radius)
    assert(a.mass === b.mass)
    assert(a.spawned === b.spawned)
    assert(a.health === b.health)
    assert(a.lifetime === b.lifetime)
    assert(a.impactDamage === b.impactDamage)
    assert(a.position === b.position)
  }

  def compareTrees(a: Tree, b: Tree): Unit = {
    assert(a.id === b.id)
    assert(a.position === b.position)
  }

  property("pickle Died") {
    pickleUnpickle[GameClientMessage](Died)
  }
  property("pickle Vec2d") {
    pickleUnpickle(Vec2d.unitUp)
  }
  property("pickle BallId") {
    pickleUnpickle(new BallId(1))
  }
  property("pickle snowball") {
    val ball2 = pickleUnpickle(ball)
    compareSnowballs(ball, ball2)
  }
  property("pickle sled") {
    val sled2 = pickleUnpickle(sled)
    compareSleds(sled, sled2)
  }
  property("pickle tree") {
    val tree = Tree()
    import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers.nullTreeTracker
    tree.position = Vec2d.unitLeft
    val tree2 = pickleUnpickle(tree)
    compareTrees(tree, tree2)
  }
  property("pickle state") {
    pickleUnpickle[GameClientMessage](
      State(1L, sleds = Seq(sled), snowballs = Seq(ball))
    )
  }
  property("pickle scoreboard") {
    pickleUnpickle[GameClientMessage](
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

  property("pickle SledType") {
    pickleUnpickle[SledType](TankSledType)
  }

  property("pickle game server Join message") {
    val join = Join("d", TankSledType, RedSkis)
    pickleUnpickle[GameServerMessage](join)
  }

}
