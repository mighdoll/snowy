import scala.collection.mutable
import socketserve.ConnectionId

/** A collection of playfield objects, indexed by connection and by screen position.
  * Only a single object per connection is permitted. */
class PlayfieldStore[A <: PlayfieldObject](size: Vec2d, gridSpacing: Double) {
  private val grid = new Grid[A](size, gridSpacing)
  private var items = mutable.Map[ConnectionId, A]()

  // --- methods that modify the store ---

  /** Run a function that replaces each item with a transformed copy */
  def replaceItems(fn: A => A): Unit = {
    items = items.map { case (id, item) =>
      id -> fn(item)
    }
  }

  /** Run a function that optionally ereplaces each item with a transformed copy */
  def replaceSomeItems(pfn: PartialFunction[A, A]): Unit = {
    items = items.map { case (id, item) =>
      if (pfn.isDefinedAt(item)) {
        id -> pfn(item)
      } else {
        id -> item
      }
    }
  }

  /** add a connection,item pair to the store */
  def add(id: ConnectionId, item: A): Unit = {
    items(id) = item
    grid.add(item)
  }

  /** remove a connection,item pair */
  def remove(id: ConnectionId): Unit = {
    items.remove(id).foreach {item =>
      grid.remove(item)
    }
  }

  /** modify one item */
  def modify(id: ConnectionId)(fn: A => A): Unit = {
    items.get(id) match {
      case Some(item) => items(id) = fn(item)
      case None       =>
    }
  }

  // --- methods that read the store, but do not modify the store ---

  /** run a function over all the connection,item pairs in the store
    *
    * @return the collected function results */
  def map[B](fn: (ConnectionId, A) => B): Traversable[B] = {
    items.map { case (id, item) => fn(id, item) }
  }

  /** run a function over all the connection,item pairs in the store */
  def foreach(fn: (ConnectionId, A) => Unit): Unit = {
    items.map { case (id, item) => fn(id, item) }
  }

  /** run a side effecting function over one item, identified by its connection */
  def forOneItem(id: ConnectionId)(fn: A => Unit): Unit = {
    items.get(id) match {
      case Some(item) => fn(item)
      case None       => println(s"forOneItem. item not found for id: $id")
    }
  }

  /** collect matching connection item pairs and apply a function
    *
    * @return the collected function results */
  def collect[B](fn: PartialFunction[(ConnectionId, A), B]): Traversable[B] = {
    items.collect(fn)
  }

  /** optionally retrieve a item by connection */
  def get(id: ConnectionId): Option[A] = items.get(id)

  /** return all items in a given area */
  def inBox(box: Rect): Set[A] = grid.inBox(box)

}

