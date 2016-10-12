import math.{ceil, floor, round}
import snowy.playfield.{PlayfieldObject, Rect}
import vector.Vec2d

object Grid {
  def apply[A <: PlayfieldObject](size: Vec2d, spacing: Double, items: TraversableOnce[A]): Grid[A] = {
    new Grid[A](size, spacing) {
      override def initialCells() = {
        val array = blankCells()
        val sorted = items.toVector.groupBy(cellIndex(_))
        sorted.foreach { case (index, cellItems) =>
          array(index) = Some(cellItems.toSet)
        }
        array
      }
    }
  }
}

class Grid[A <: PlayfieldObject](val size: Vec2d, val spacing: Double) {
  private val columns = ceil(size.x / spacing).toInt
  protected val cells: Vector[Option[Set[A]]] = {
    initialCells().toVector
  }

  protected def initialCells(): Array[Option[Set[A]]] = blankCells()

  protected def blankCells(): Array[Option[Set[A]]] = {
    val cellCount = rawIndex(size.x, size.y) + 1
    val array = new Array[Option[Set[A]]](cellCount)
    (0 until cellCount).foreach { index =>
      array(index) = None
    }
    array
  }

  def add(item: A): Grid[A] = {
    val index = cellIndex(item)
    val newSet =
      cells(index) match {
        case Some(set) => set + item
        case None      => Set(item)
      }
    val newCells = cells.updated(index, Some(newSet))
    new Grid[A](size, spacing) {
      override val cells = newCells
    }
  }

  def remove(item: A): Grid[A] = {
    val index = cellIndex(item)
    val newCells =
      cells(index) match {
        case Some(set) if set.size > 1  => cells.updated(index, Some(set - item))
        case Some(set) if set.size == 1 => cells.updated(index, None)
        case None                       => cells
      }
    new Grid[A](size, spacing) {
      override val cells = newCells
    }
  }

  def inBox(box: Rect): Set[A] = ???

  def itemCount: Int = cells.flatten.map(_.size).sum

  protected def cellIndex(playfieldObject: PlayfieldObject): Int =
    rawIndex(playfieldObject.pos.x, playfieldObject.pos.y)

  protected def rawIndex(x: Double, y: Double): Int = {
    val row = floor(y / spacing).toInt
    val column = floor(x / spacing).toInt
    row * columns + column
  }
}

