import scala.collection.mutable
import socketserve.ConnectionId

/** a collection of playfield objects, indexed by connection and by screen position */
class PlayfieldStore[A <: PlayfieldObject](size: Vec2d, gridSpacing: Double) {
  private val grid = new Grid[A](size, gridSpacing)
  private var items = mutable.Map[ConnectionId, A]()

  /** Run a function that replaces each item with a transformed copy */
  def replaceItems(fn: A => A): Unit = {
    items = items.map { case (id, item) =>
      id -> fn(item)
    }
  }

  /** run a function over all the connection,sled pairs in the store
    * @return the collected function results */
  def map[B](fn: (ConnectionId, A) => B): Traversable[B] = {
    items.map { case (id, item) => fn(id, item) }
  }

  /** run a function over all the connection,sled pairs in the store */
  def foreach(fn: (ConnectionId, A) => Unit): Unit = {
    items.map { case (id, item) => fn(id, item) }
  }

  /** run a side effecting function over one sled, identified by its connection */
  def forOneItem(id:ConnectionId)(fn: A => Unit): Unit = {
    items.get(id) match {
      case Some(item) => fn(item)
      case None       => println(s"forOneItem. item not found for id: $id")
    }
  }

  /** collect matching connection sled pairs and apply a function
    * @return the collected function results */
  def collect[B](fn: PartialFunction[(ConnectionId, A), B]): Traversable[B] = {
    items.collect(fn)
  }

  /** add a connection,sled pair to the store */
  def add(id: ConnectionId, item: A): Unit = {
    items(id) = item
    grid.add(item)
  }

  /** modify one item */
  def modify(id: ConnectionId)(fn: A => A): Unit = {
    items.get(id) match {
      case Some(item) => items(id) = fn(item)
      case None       => println(s"modify. item not found for id: $id")
    }
  }

  /** remove a connection,sled pair */
  def remove(id: ConnectionId): Unit = {
    items -= id
  }

  /** optionally retrieve a sled by connection */
  def get(id: ConnectionId): Option[A] = items.get(id)

  /** return all sleds in a given area */
  def inBox(box:Rect): Set[A] = grid.inBox(box)

}

