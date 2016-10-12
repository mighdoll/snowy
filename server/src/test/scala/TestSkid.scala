import org.scalacheck.Prop._
import org.scalacheck._
import org.scalacheck.Gen._
import snowy.GameConstants._
import math.Pi
import snowy.playfield.Skid
import vector.Vec2d

object RichTestVec2d {
  val epsilon = .00001

  implicit class TestingVec2d(a: Vec2d) {

    import math.abs

    def approxEquals(b: Vec2d): Prop = {
      abs(a.x - b.x) < epsilon && abs(a.y - b.y) < epsilon ?= true
    }
  }

}

import RichTestVec2d._

object TestSkid extends Properties("Skid") {
  val skid100 = new Skid(1.0)
  val skid10 = new Skid(.1)
  val skid50 = new Skid(.5)

  property("up the screen, no rotation => up the screen") = {
    skid10(Vec2d.unitDown, Pi) approxEquals (Vec2d.unitDown)
  }
  property("up the screen, 180 rotation => up the screen") = {
    skid10(Vec2d.unitDown, 0) approxEquals (Vec2d.unitDown)
  }
  property("down the screen, 180 rotation => down the screen") = {
    skid10(Vec2d.unitUp, Pi) approxEquals (Vec2d.unitUp)
  }
  property("down the screen, no rotation => down the screen") = {
    skid10(Vec2d.unitUp, 0) approxEquals (Vec2d.unitUp)
  }
  property("right, no rotation => right") = {
    skid10(Vec2d.unitRight, Pi / 2) approxEquals (Vec2d.unitRight)
  }
  property("downscreen at max speed, skis rotated at 45, 100% of skid => 45") = {
    val result = skid100(Vec2d.unitUp * maxSpeed, rotation = Pi / 4)
    result approxEquals Vec2d.fromRotation(Pi / 4) * maxSpeed
  }
  property("downscreen at max speed, skis rotated at 45, 50% of skid => 22.5") = {
    val result = skid50(Vec2d.unitUp * maxSpeed, rotation = Pi / 4)
    result.unit approxEquals Vec2d.fromRotation(Pi / 4 / 2)
  }
  property("downscreen at max speed, skis rotated at 45, 10% of skid => 4.5") = {
    val result = skid10(Vec2d.unitUp * maxSpeed, rotation = Pi / 4)
    result.unit approxEquals Vec2d.fromRotation(.1 * Pi / 4)
  }
  property("downscreen at max speed, skis rotated at 45+180, 10% of skid => 4.5") = {
    val result = skid10(Vec2d.unitUp * maxSpeed, rotation = Pi + Pi / 4)
    result.unit approxEquals Vec2d.fromRotation(.1 * Pi / 4)
  }
  property("upscreen 45 at max speed, skis rotated straight up, 100% of skid => up") = {
    val initialAngle = Pi / 2 + Pi / 4
    val result = skid100(Vec2d.fromRotation(initialAngle) * maxSpeed, rotation = Pi)
    result.unit approxEquals Vec2d.unitDown
  }
  property("upscreen 45 at max speed, skis rotated straight up, 10% of skid => 4.5") = {
    val initialAngle = Pi * 3 / 4
    val result = skid10(Vec2d.fromRotation(initialAngle) * maxSpeed, rotation = Pi)
    val expectedAngle = initialAngle + (Pi - initialAngle) *  .1
    result.unit approxEquals Vec2d.fromRotation(expectedAngle)
  }
  property("upscreen -45 at max speed, skis rotated straight up, 100% of skid => up") = {
    val initialAngle = Pi + Pi / 4
    val result = skid100(Vec2d.fromRotation(initialAngle) * maxSpeed, rotation = Pi)
    result.unit approxEquals Vec2d.unitDown
  }

}
