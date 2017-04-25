package snowy.playfield

import vector.Vec2d

trait CircularObject[A <: PlayfieldItem[A]] extends MovableItem[A] with Bounds {
  self: A =>

  def radius: Double

  def mass: Double

  override def boundingBox = Rect(position - Vec2d(radius, radius), Vec2d(radius * 2, radius * 2))
}

trait MovableItem[A <: PlayfieldItem[A]] extends PlayfieldItem[A] {
  self: A =>
  var speed: Vec2d
}

