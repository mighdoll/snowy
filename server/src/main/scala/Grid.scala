import scala.collection.mutable.HashSet
import math.{ceil, round}

class Grid[A <: PlayfieldObject](size: Vec2d, spacing: Double) {
  val columns = ceil(size.x / spacing).toInt
  val cells: Array[HashSet[A]] = {
    val cellCount = columns * ceil(size.y / spacing).toInt
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

  private def cell(obj: A): HashSet[A] = {
    val column = round(obj.pos.x / spacing).toInt
    val row = round(obj.pos.y / spacing).toInt
    cells(row * columns + column)
  }

}


