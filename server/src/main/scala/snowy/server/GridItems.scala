package snowy.server

import scala.collection.mutable
import snowy.playfield.{Grid, Playfield, PlayfieldItem}

trait GridItems[A <: PlayfieldItem[A]] {
  protected val playfield: Playfield
  private val gridSpacing  = 100.0

  implicit val powerUpGrid = new Grid[A](playfield.size, gridSpacing)
  val items = mutable.HashSet[A]()
}

