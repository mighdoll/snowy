package snowy.playfield

import scala.collection.mutable.HashSet
import scala.math.{ceil, floor}
import snowy.playfield.Intersect._
import snowy.util.DoubleUtil._
import vector.Vec2d

/** A rectangular grid of cells that contain PlayfieldItems.
  * Each cell contains the items whose bounding boxes overlap that cell.
  * The grid is intended to speed collision detection, by
  * returning only the playfield items in a given area.
  *
  * Note that a playfield item can be stored in multiple cells if
  * it overlaps multiple cells.
  * @param size size in playfield pixels that the grid covers
  * @param spacing grid cells are squares convering this number of pixels on a side
  */
class Grid[A <: PlayfieldItem[A]](val size: Vec2d, val spacing: Double)
    extends PlayfieldTracker[A] {

  private val columns = ceil(size.x / spacing).toInt

  private val cells: Array[HashSet[A]] = {
    val lastCell = coordinatesToCellIndex(size.x, size.y)
    val array    = new Array[HashSet[A]](lastCell + 1)
    (0 to lastCell).foreach { index =>
      array(index) = HashSet()
    }
    array
  }

  override def add(item: A): Unit = {
    for (cell <- coveredCells(item.boundingBox)) {
      val added = cell.add(item)
      if (!added) {
        println(s"adding item $item already in the grid") // TODO log that works on client too
      }
    }
  }

  /** Remove an item from the grid.
    *
    * Note that the bounding box of the item must unchanged since insertion
    */
  override def remove(item: A): Unit = {
    for (cell <- coveredCells(item.boundingBox)) {
      val found =
        cell.remove(item)
      if (!found) {
        println(s"removing item $item not in the grid") // TODO log that works on client too
        items.find(_ == item) match {
          case Some(_) => println(s"$item in another cell in the grid")
          case None    => println(s"$item not in any other cell in the grid")
        }
      }
    }
  }

  /** @return all of the items inside a bounding box */
  def inside(bounds: Rect): Iterable[A] = {
    val found =
      for {
        cell <- coveredCells(bounds)
        item <- cell
        if bounds.intersectRect(item.boundingBox)
      } yield {
        item
      }
    found.toSet
  }

  def items: Traversable[A] = {
    val list =
      for {
        cell <- cells
        item <- cell
      } yield item

    list.toSet
  }

  private def coveredCells(bounds: Rect): Iterable[HashSet[A]] = {
    val firstRow    = cellRow(bounds.top)
    val lastRow     = cellRow(bounds.bottom)
    val firstColumn = cellColumn(bounds.left)
    val lastColumn  = cellColumn(bounds.right)
    for {
      row    <- firstRow to lastRow
      column <- firstColumn to lastColumn
      index = cellRowColumnToIndex(row, column)
    } yield cells(index)

  }

  private def cellIndex(playfieldItem: A): Int =
    coordinatesToCellIndex(playfieldItem.position.x, playfieldItem.position.y)

  private def coordinatesToCellIndex(x: Double, y: Double): Int = {
    val column = cellColumn(x)
    val row    = cellRow(y)
    cellRowColumnToIndex(row, column)
  }

  private def cellRowColumnToIndex(row: Int, column: Int): Int = {
    (row * columns) + column
  }

  private def clipEndExclusive(coord: Double, max: Double): Double = {
    // we want the range to go [0 max).  i.e. exclusive of the max value
    val cappedMax = max - .1 // LATER use a less hacky value here.
    coord.clip(0, cappedMax)
  }

  private def cellRow(y: Double): Int = {
    val boundedY = clipEndExclusive(y, size.y)
    floor(boundedY / spacing).toInt
  }

  private def cellColumn(x: Double): Int = {
    val boundedX = clipEndExclusive(x, size.x)
    floor(boundedX / spacing).toInt
  }

}
