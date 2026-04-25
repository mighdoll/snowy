package snowy.playfield

import org.scalatest.propspec.AnyPropSpec
import upickle.default.{readBinary, writeBinary, ReadWriter}
import snowy.{playIdRW, PlayIdTag}
import snowy.PlayIdTag.given
import snowy.GameClientProtocol.*
import snowy.GameServerProtocol.{GameServerMessage, Join}
import snowy.playfield.PlayId.BallId
import snowy.playfield.SnowballFixture.testSnowball
import vector.Vec2d

class TestBooPickle extends AnyPropSpec {
  val sled = Sled.dummy
  val ball = testSnowball()

  def pickleUnpickle[T: ReadWriter](value: T): T = {
    val bytes     = writeBinary[T](value)
    val unpickled = readBinary[T](bytes)
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
    assert(a.lastShotTime === b.lastShotTime)
    assert(a.lastBoostTime === b.lastBoostTime)
    assert(a.mass === b.mass)
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
    import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers.nullTreeTracker
    val tree  = Tree(Vec2d.unitLeft)
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
    val bytes  = writeBinary[GameClientMessage](Ping)
    val result = readBinary[GameClientMessage](bytes)
    assert(result === Ping)
  }

  property("pickle game client message State and Ping") {
    val state      = State(1L, sleds = Seq(sled), snowballs = Seq(ball))
    val stateBytes = writeBinary[GameClientMessage](state)
    val unpickledState: GameClientMessage =
      readBinary[GameClientMessage](stateBytes)

    val pingBytes = writeBinary[GameClientMessage](Ping)
    val unpickledPing: GameClientMessage =
      readBinary[GameClientMessage](pingBytes)

    unpickledState match {
      case _: State =>
      case _        => fail()
    }

    unpickledPing match {
      case Ping =>
      case _    => fail()
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
