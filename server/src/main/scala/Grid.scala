import scala.collection.mutable.HashSet
import math.{ceil, round}

class Grid[A <: PlayfieldObject](size: Vec2d, spacing: Double) {
  val columns = ceil(size.x / spacing).toInt
  val cells: Array[HashSet[A]] = {
    val cellCount = cellIndex(size.x, size.y)
    val array = new Array[HashSet[A]](cellCount)
    (0 until cellCount).foreach { index =>
      array(index) = HashSet[A]()
    }
    array
  }

  def add(elem: A): Unit = {
    cell(elem).add(elem)
  }

  def remove(elem: A): Unit = {
    cell(elem).remove(elem)
  }

  def inBox(box:Rect): Set[A] = ???

  private def cellIndex(x:Double, y:Double):Int = {
    val column = round(x % spacing).toInt
    val row = round(y / spacing).toInt
    row * columns + column
  }

  private def cell(obj: A): HashSet[A] = {
    val index = cellIndex(obj.pos.x, obj.pos.y)
    if (index >= cells.length) {
      // TODO fixme
      println(s"Grid.cell bug.  index: $index  cells.length: ${cells.length}")
      println(s"   x: ${obj.pos.x}  y: ${obj.pos.y}")
      new HashSet()
    } else {
      cells(index)
    }
  }

}


