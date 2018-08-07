package snowy.playfield

import org.scalatest.PropSpec
import snowy.collision.Collisions._
import vector.Vec2d

class TestCircleRectCollision extends PropSpec {
  property("Encompassing circle") {
    assert(
      circleRectCollide(Circle(Vec2d(0, 0), 20), Rect(Vec2d(-10, -10), Vec2d(20, 20)))
    )
  }

  property("Encompassing rect") {
    assert(
      circleRectCollide(Circle(Vec2d(0, 0), 5), Rect(Vec2d(-10, -10), Vec2d(20, 20)))
    )
  }

  property("Circle size 0") {
    assert(
      circleRectCollide(Circle(Vec2d(0, 0), 0), Rect(Vec2d(-10, -10), Vec2d(20, 20)))
    )
  }

  property("Rect size 0") {
    assert(circleRectCollide(Circle(Vec2d(0, 0), 10), Rect(Vec2d(0, 0), Vec2d(0, 0))))
  }

  property("Intersect on right") {
    assert(circleRectCollide(Circle(Vec2d(0, 0), 10), Rect(Vec2d(7, 7), Vec2d(1, 1))))
  }

  property("Near intersect on right") {
    assert(!circleRectCollide(Circle(Vec2d(0, 0), 10), Rect(Vec2d(8, 8), Vec2d(1, 1))))
  }

  property("Intersect on left") {
    assert(circleRectCollide(Circle(Vec2d(0, 0), 10), Rect(Vec2d(-7, -7), Vec2d(1, 1))))
  }

  property("Near intersect on left") {
    assert(!circleRectCollide(Circle(Vec2d(0, 0), 10), Rect(Vec2d(-9, -9), Vec2d(1, 1))))
  }

  property("Near y, far x, shouldn't intersect") {
    assert(
      !circleRectCollide(
        Circle(Vec2d(2478, 7998), 35),
        Rect(Vec2d(728, 7928), Vec2d(10, 40))
      )
    )
  }
}
