package snowy.playfield

import org.scalatest.PropSpec
import snowy.collision.CollideThings.collideCollection
import snowy.playfield.PlayfieldTracker.{nullSledTracker, nullSnowballTracker}
import snowy.playfield.SnowballFixture.testSnowball
import vector.Vec2d

class TestCircleCircleCollision extends PropSpec {
  property("hit from right") {
    val a = TestCircle(Vec2d.zero, Vec2d.zero)
    val b = TestCircle(Vec2d(4, 0), Vec2d(-1, 0))
    collideCollection(Seq(a, b))(TestCircle.nullCircleTracker)
    // TODO test
  }

  property("hit from right, up a bit") {
    implicit val _s = nullSledTracker
    implicit val _b = nullSnowballTracker
    val a           = Sled("te")
    val b           = testSnowball()
    b.position = Vec2d(5, 1)
    b.speed = Vec2d(-1, 0)
//    collideCollection(Seq(a, b))(TestCircle.nullCircleTracker) // TODO tricky to get the typing right..
//    println(s"$a $b")
    // TODO test
  }

}

case class TestCircle(private val initialPosition: Vec2d, override var speed: Vec2d)
    extends CircularObject[TestCircle] {

  override def boundingBox = Rect(position, Vec2d(radius * 2, radius * 2))

  override var health: Double = 1

  override def armor: Double = 1

  override def impactDamage: Double = 1

  override def radius: Double = 18

  override def mass: Double = 1

  override def toString(): String = s"pos:$position  speed:$speed"

  position = initialPosition

}

object TestCircle {

  implicit val nullCircleTracker = PlayfieldTracker.nullTracker[TestCircle]
}
