package snowy.playfield

import vector.Vec2d

trait MovableCircularItem[A <: PlayfieldItem[A]]
    extends CircularItem[A] with MovableItem[A] { self: A => }

trait CircularItem[A <: PlayfieldItem[A]] extends PlayfieldItem[A] { self: A =>

  def radius: Double

  override def boundingBox =
    Rect(position - Vec2d(radius, radius), Vec2d(radius * 2, radius * 2))
}

trait MovableItem[A <: PlayfieldItem[A]] extends PlayfieldItem[A] { self: A =>
  var speed: Vec2d
  var health: Double
  def mass: Double
}
