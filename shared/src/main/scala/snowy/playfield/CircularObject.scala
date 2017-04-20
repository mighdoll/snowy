package snowy.playfield

import vector.Vec2d

trait CircularObject[A <: PlayfieldItem[A]] extends MoveablePlayfieldObject[A] with Bounds {
  self: A =>

  def radius: Double

  def mass: Double

}

trait MoveablePlayfieldObject[A <: PlayfieldItem[A]] extends PlayfieldItem[A] {
  self: A =>
  var speed: Vec2d
}

