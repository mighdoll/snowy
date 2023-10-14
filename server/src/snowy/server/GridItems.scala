package snowy.server

import snowy.playfield.{Grid, Playfield, PlayfieldItem}

import scala.collection.mutable
import scala.reflect.ClassTag

trait GridItems[A <: PlayfieldItem[A]] {
  protected val playfield: Playfield
  private val gridSpacing = 100.0

  implicit val grid: Grid[A] = new Grid[A](playfield.size, gridSpacing)
  val items                  = mutable.HashSet[A]()

  def remove(item: A)(implicit ct: ClassTag[A]): Unit = {
    grid.remove(item)
    items.remove(item)
  }

  def ++=(addItems: Iterable[A]): Unit = {
    items ++= addItems
    addItems.foreach(grid.add(_))
  }
}
