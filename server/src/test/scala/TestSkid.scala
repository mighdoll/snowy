import org.scalacheck.Prop._
import org.scalacheck._
import org.scalacheck.Gen._
import GameConstants._
import math.Pi

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
  val skidTime = .1

  property("up the screen, no rotation => up the screen") = {
    Skid.skid(Vec2d.unitDown, Pi, skidTime) approxEquals (Vec2d.unitDown)
  }
  property("up the screen, 180 rotation => up the screen") = {
    Skid.skid(Vec2d.unitDown, 0, skidTime) approxEquals (Vec2d.unitDown)
  }
  property("down the screen, 180 rotation => down the screen") = {
    Skid.skid(Vec2d.unitUp, Pi, skidTime) approxEquals (Vec2d.unitUp)
  }
  property("down the screen, no rotation => down the screen") = {
    Skid.skid(Vec2d.unitUp, 0, skidTime) approxEquals (Vec2d.unitUp)
  }
  property("right, no rotation => right") = {
    Skid.skid(Vec2d.unitRight, Pi / 2, skidTime) approxEquals (Vec2d.unitRight)
  }
  property("down at max speed, skis rotated at 45, 100% of skid => 45") = {
    val result = Skid.skid(Vec2d.unitUp * maxSpeed, rotation = Pi / 4, skidTime = 1)
    result approxEquals Vec2d.fromRotation(Pi / 4) * maxSpeed
  }
  property("down at max speed, skis rotated at 45, 50% of skid => 22.5") = {
    val result = Skid.skid(Vec2d.unitUp * maxSpeed, rotation = Pi / 4, skidTime = .5)
    result.unit approxEquals Vec2d.fromRotation(Pi / 4 / 2)
  }
  property("down at max speed, skis rotated at 45, 10% of skid => 4.5") = {
    val percent = .1
    val result = Skid.skid(Vec2d.unitUp * maxSpeed, rotation = Pi / 4, skidTime = percent)
    result.unit approxEquals Vec2d.fromRotation(percent * Pi / 4)
  }
  property("down at max speed, skis rotated at 45+180, 10% of skid => 4.5") = {
    val percent = .1
    val result = Skid.skid(Vec2d.unitUp * maxSpeed, rotation = Pi + Pi / 4, skidTime = percent)
    result.unit approxEquals Vec2d.fromRotation(percent * Pi / 4)
  }
  property("up 45 at max speed, skis rotated straight up, 100% of skid => up") = {
    val percent = 1
    val initialAngle = Pi / 2 + Pi / 4
    val result = Skid.skid(Vec2d.fromRotation(initialAngle) * maxSpeed, rotation = Pi, skidTime = percent)
    result.unit approxEquals Vec2d.unitDown
  }
  property("up 45 at max speed, skis rotated straight up, 10% of skid => 4.5") = {
    val percent = .1
    val initialAngle = Pi * 3 / 4
    val result = Skid.skid(Vec2d.fromRotation(initialAngle) * maxSpeed, rotation = Pi, skidTime = percent)
    val expectedAngle = initialAngle + (Pi - initialAngle) * percent
    result.unit approxEquals Vec2d.fromRotation(expectedAngle)
  }

  property("up -45 at max speed, skis rotated straight up, 100% of skid => up") = {
    val percent = 1
    val initialAngle = Pi + Pi / 4
    val result = Skid.skid(Vec2d.fromRotation(initialAngle) * maxSpeed, rotation = Pi, skidTime = percent)
    result.unit approxEquals Vec2d.unitDown
  }

}
