import scala.collection.mutable.HashSet
import math.{ceil, round, floor}

class Grid[A <: PlayfieldObject](size: Vec2d, spacing: Double) {
  val columns = ceil(size.x / spacing).toInt
  val cells: Array[HashSet[A]] = {
    val cellCount = cellIndex(size.x, size.y) + 1
    val array = new Array[HashSet[A]](cellCount)
    (0 until cellCount).foreach { index =>
      array(index) = HashSet[A]()
    }
    array
  }

  def add(elem: A): Unit = {
    cell(elem).add(elem)
  }

  def remove(elem: A): Boolean = {
    cell(elem).remove(elem)
  }

  def inBox(box:Rect): Set[A] = ???

  private def cellIndex(x:Double, y:Double):Int = {
    val row = floor(y / spacing).toInt
    val column = floor(x / spacing).toInt
    row * columns + column
  }

  private def cell(obj: A): HashSet[A] = {
    val index = cellIndex(obj.pos.x, obj.pos.y)
    cells(index)
  }

}


