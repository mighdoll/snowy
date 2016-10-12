import Collisions._
import org.scalacheck.Prop._
import org.scalacheck._
import vector.Vec2d


object TestCollisions extends Properties("Collisions") {
  property("Encompassing circle") = {
    circleRectCollide(Circle(Vec2d(0, 0), 20), Rect(Vec2d(-10, -10), Vec2d(20, 20))) =? true
  }

  property("Encompassing rect") = {
    circleRectCollide(Circle(Vec2d(0, 0), 5), Rect(Vec2d(-10, -10), Vec2d(20, 20))) =? true
  }

  property("Circle size 0") = {
    circleRectCollide(Circle(Vec2d(0, 0), 0), Rect(Vec2d(-10, -10), Vec2d(20, 20))) =? true
  }

  property("Rect size 0") = {
    circleRectCollide(Circle(Vec2d(0, 0), 10), Rect(Vec2d(0, 0), Vec2d(0, 0))) =? true
  }

  property("Intersect on right") = {
    circleRectCollide(Circle(Vec2d(0, 0), 10), Rect(Vec2d(7, 7), Vec2d(1, 1))) =? true
  }

  property("Near intersect on right") = {
    circleRectCollide(Circle(Vec2d(0, 0), 10), Rect(Vec2d(8, 8), Vec2d(1, 1))) =? false
  }

  property("Intersect on left") = {
    circleRectCollide(Circle(Vec2d(0, 0), 10), Rect(Vec2d(-7, -7), Vec2d(1, 1))) =? true
  }

  property("Near intersect on left") = {
    circleRectCollide(Circle(Vec2d(0, 0), 10), Rect(Vec2d(-9, -9), Vec2d(1, 1))) =? false
  }

  property("Near y, far x, shouldn't intersect") = {
    circleRectCollide(Circle(Vec2d(2478, 7998),35), Rect(Vec2d(728,7928),Vec2d(10,40))) =? false
  }
}
