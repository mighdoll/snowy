import scala.collection.mutable
import socketserve.ConnectionId

/** A collection of playfield objects, indexed by connection and by screen position.
  * Multiple objects per connection are expected. */
class PlayfieldMultiMap[A <: PlayfieldObject](size: Vec2d, gridSpacing: Double) {
  private var grid = new Grid[A](size, gridSpacing)
  private var items = mutable.Map[ConnectionId, mutable.ListBuffer[A]]()

  // --- methods that modify the store ---

  /** remove all items matching a predicate */
  def removeMatchingItems(fn: A => Boolean): Traversable[A] = {
    grid = new Grid[A](size, gridSpacing)
    val removals =
      for {
        (id, list) <- items
        item <- list
        if fn(item)
      } yield {
        (id, item)
      }

    removals.foreach { case (id, item) =>
      remove(id, item)
    }
    removals.map { case (id, item) => item }
  }

  /** Run a function that replaces each item with a transformed copy */
  def replaceItems(fn: A => A): Unit = {
    items = items.map{case (id, list) =>
       val newList = list.map {item =>
         fn(item)
       }
      (id, newList)
    }
  }

  /** add a connection,item pair to the store */
  def add(id: ConnectionId, item: A): Unit = {
    items.get(id) match {
      case Some(buffer) => buffer.append(item)
      case None         => items(id) = mutable.ListBuffer(item)
    }
    grid.add(item)
  }

  /** remove a connection,item pair from the store */
  def remove(id: ConnectionId, item: A): Boolean = {
    items.get(id) match {
      case Some(buffer) if buffer.contains(item) =>
        buffer.remove(buffer.indexOf(item))
        if (buffer.isEmpty) {
          items.remove(id)
        }
        grid.remove(item)
        true
      case _                                     =>
        false
    }
  }

  // --- methods that are read only on the store ---

  /** run a function over all the items in the store */
  def foreachItem(fn: A => Unit):Unit = {
    for {
      (id, list) <- items
      item <- list
    } fn(item)
  }

  /** run a function over all the connection,item pairs in the store
    *
    * @return the collected function results */
  def map[B](fn: (ConnectionId, A) => B): Traversable[B] = {
    val results = for {
      (id, list) <- items
      item <- list
    } yield {
      fn(id, item)
    }
    results.toList
  }

  def debugPrint(prefix:String = "items"): Unit = {
    val balls = items.flatMap(_._2).mkString("", "\r\n  ", "")
  }


}
