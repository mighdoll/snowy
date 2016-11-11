package snowy.playfield

trait FlyingCircle extends MoveablePlayfieldObject {

  def radius: Double

  def mass: Double

}

trait CircularObject extends MoveablePlayfieldObject with FlyingCircle
