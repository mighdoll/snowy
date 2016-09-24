import scala.collection.mutable.HashSet
import math.{ceil, round}
import scala.collection.mutable
import socketserve.ConnectionId

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

  def inBox(topLeft: Vec2d, bottomRight: Vec2d): Set[A] = ???

  private def cell(obj: A): HashSet[A] = {
    val column = round(obj.pos.x / spacing).toInt
    val row = round(obj.pos.y / spacing).toInt
    cells(row * columns + column)
  }

}

class SledStore(size: Vec2d, gridSpacing: Double) {
  private val grid = new Grid[SledState](size, gridSpacing)
  private var sleds = mutable.Map[ConnectionId, SledState]()

  /** Run a function that replaces each sled with a transformed copy */
  def mapSleds(fn: SledState => SledState): Unit = {
    sleds = sleds.map { case (id, sled) =>
      id -> fn(sled)
    }
  }

  def map[A](fn: (ConnectionId, SledState) => A): Traversable[A] = {
    sleds.map { case (id, sled) => fn(id, sled) }
  }

  def filter(fn: (ConnectionId, SledState) => Boolean): Traversable[(ConnectionId, SledState)] = {
    sleds.filter { case (id, sled) => fn(id, sled) }
  }

  def collect[A](fn: PartialFunction[(ConnectionId, SledState), A]): Traversable[A] = {
    sleds.collect(fn)
  }

  def add(id: ConnectionId, sled: SledState): Unit = {
    sleds(id) = sled
    grid.add(sled)
  }

  def modify(id: ConnectionId)(fn: SledState => SledState): Unit = {
    sleds.get(id) match {
      case Some(sled) => sleds(id) = fn(sled)
      case None       => println(s"modify. sled not found for id: $id")
    }
  }

  def remove(id: ConnectionId): Unit = {
    sleds -= id
  }

  def get(id: ConnectionId): Option[SledState] = sleds.get(id)

  def inBox(topLeft: Vec2d, bottomRight: Vec2d): Set[SledState] =
    grid.inBox(topLeft, bottomRight)

}
