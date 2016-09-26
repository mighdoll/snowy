import scala.collection.mutable
import socketserve.ConnectionId

/** a collection of sleds, indexed by connection and by screen position */
class SledStore(size: Vec2d, gridSpacing: Double) {
  private val grid = new Grid[SledState](size, gridSpacing)
  private var sleds = mutable.Map[ConnectionId, SledState]()

  /** Run a function that replaces each sled with a transformed copy */
  def mapSleds(fn: SledState => SledState): Unit = {
    sleds = sleds.map { case (id, sled) =>
      id -> fn(sled)
    }
  }

  /** run a function over all the connection,sled pairs in the store
    * @return the collected function results */
  def map[A](fn: (ConnectionId, SledState) => A): Traversable[A] = {
    sleds.map { case (id, sled) => fn(id, sled) }
  }

  /** run a side effecting function over one sled, identified by its connection */
  def forOneSled[A](id:ConnectionId) (fn: SledState => Unit): Unit = {
    sleds.get(id) match {
      case Some(sled) => fn(sled)
      case None       => println(s"forOneSled. sled not found for id: $id")
    }
  }

  /** collect matching connection sled pairs and apply a function
    * @return the collected function results */
  def collect[A](fn: PartialFunction[(ConnectionId, SledState), A]): Traversable[A] = {
    sleds.collect(fn)
  }

  /** add a connection,sled pair to the store */
  def add(id: ConnectionId, sled: SledState): Unit = {
    sleds(id) = sled
    grid.add(sled)
  }

  /** movify a sled */
  def modify(id: ConnectionId)(fn: SledState => SledState): Unit = {
    sleds.get(id) match {
      case Some(sled) => sleds(id) = fn(sled)
      case None       => println(s"modify. sled not found for id: $id")
    }
  }

  /** remove a connection,sled pair */
  def remove(id: ConnectionId): Unit = {
    sleds -= id
  }

  /** optionally retrieve a sled by connection */
  def get(id: ConnectionId): Option[SledState] = sleds.get(id)

  /** return all sleds in a given area */
  def inBox(topLeft: Vec2d, bottomRight: Vec2d): Set[SledState] =
    grid.inBox(topLeft, bottomRight)

}
