import org.scalatest.PropSpec
import snowy.collision.CollideThings.collideCollection
import snowy.playfield.{CircularObject, PlayId, PlayfieldObject, Sled}
import snowy.playfield.SnowballFixture.testball
import vector.Vec2d

class TestCircleCircleCollision extends PropSpec {
  property("hit from right") {
    val a = TestCircle(Vec2d.zero, Vec2d.zero)
    val b = TestCircle(Vec2d(4, 0), Vec2d(-1, 0))
    collideCollection(Seq(a, b))
    // TODO test
  }

  property("hit from right, up a bit") {
    val a = Sled("te")
    val b = testball().copy(_position = Vec2d(5, 1), speed = Vec2d(-1, 0))
    collideCollection(Seq(a, b))
//    println(s"$a $b")
    // TODO test
  }

}

case class TestCircle(private val initialPosition: Vec2d, override var speed: Vec2d)
    extends CircularObject {
  override type MyType = TestCircle
  override def id: PlayId[TestCircle] = PlayfieldObject.nextId()

  override protected var _position: Vec2d = initialPosition

  override var health: Double = 1

  override def armor: Double = 1

  override def impactDamage: Double = 1

  override def radius: Double = 18

  override def copyWithUpdatedPos(newPos: Vec2d): TestCircle = ???

  override def mass: Double = 1

  override def toString():String = s"pos:$pos  speed:$speed"
}
