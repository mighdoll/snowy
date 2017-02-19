package snowy.playfield

import scala.math.Pi
import org.scalactic.Equality
import org.scalactic.Tolerance._
import org.scalatest.PropSpec
import snowy.GameConstants
import snowy.playfield.Skid.skid
import vector.Vec2d

class TestSkid extends PropSpec {
  implicit val equals = new Equality[Vec2d] {
    val tolerance = .0001
    override def areEqual(a: Vec2d, b: Any): Boolean = {
      b match {
        case Vec2d(x, y) => (a.x === x +- tolerance) && (a.y === y +- tolerance)
        case _           => false
      }
    }
  }
  val maxSpeed = 1000

  def skidTest(current: Vec2d, rotation: Double, percentMax: Double): Vec2d = {
    skid(
      current = current,
      rotation = rotation,
      maxSpeed = maxSpeed,
      mass = 1.0,
      deltaSeconds = percentMax / GameConstants.maxSkidTime
    )
  }

  property("up the screen, no rotation => up the screen") {
    assert(skidTest(Vec2d.unitDown, Pi, .1) === Vec2d.unitDown)
  }
  property("up the screen, 180 rotation => up the screen") {
    assert(skidTest(Vec2d.unitDown, 0, .1) === Vec2d.unitDown)
  }
  property("down the screen, 180 rotation => down the screen") {
    assert(skidTest(Vec2d.unitUp, Pi, .1) === Vec2d.unitUp)
  }
  property("down the screen, no rotation => down the screen") {
    assert(skidTest(Vec2d.unitUp, 0, .1) === Vec2d.unitUp)
  }
  property("right, no rotation => right") {
    assert(skidTest(Vec2d.unitRight, Pi / 2, .1) === Vec2d.unitRight)
  }
  property("downscreen at max speed, skis rotated at 45, 100% of skid => 45") {
    val result = skidTest(Vec2d.unitUp * maxSpeed, Pi / 4, 1)
    assert(result === Vec2d.fromRotation(Pi / 4) * maxSpeed)
  }
  property("downscreen at max speed, skis rotated at 45, 50% of skid => 22.5") {
    val result = skidTest(Vec2d.unitUp * maxSpeed, Pi / 4, .5)
    assert(result.unit === Vec2d.fromRotation(Pi / 4 / 2))
  }
  property("downscreen at max speed, skis rotated at 45, 10% of skid => 4.5") {
    val result = skidTest(Vec2d.unitUp * maxSpeed, Pi / 4, .1)
    assert(result.unit === Vec2d.fromRotation(.1 * Pi / 4))
  }
  property("downscreen at max speed, skis rotated at 45+180, 10% of skid => 4.5") {
    val result = skidTest(Vec2d.unitUp * maxSpeed, Pi + Pi / 4, .1)
    assert(result.unit === Vec2d.fromRotation(.1 * Pi / 4))
  }
  property("upscreen 45 at max speed, skis rotated straight up, 100% of skid => up") {
    val initialAngle = Pi / 2 + Pi / 4
    val result       = skidTest(Vec2d.fromRotation(initialAngle) * maxSpeed, Pi, 1)
    assert(result.unit === Vec2d.unitDown)
  }
  property("upscreen 45 at max speed, skis rotated straight up, 10% of skid => 4.5") {
    val initialAngle  = Pi * 3 / 4
    val result        = skidTest(Vec2d.fromRotation(initialAngle) * maxSpeed, Pi, .1)
    val expectedAngle = initialAngle + (Pi - initialAngle) * .1
    assert(result.unit === Vec2d.fromRotation(expectedAngle))
  }
  property("upscreen -45 at max speed, skis rotated straight up, 100% of skid => up") {
    val initialAngle = Pi + Pi / 4
    val result       = skidTest(Vec2d.fromRotation(initialAngle) * maxSpeed, Pi, 1)
    assert(result.unit === Vec2d.unitDown)
  }

}
