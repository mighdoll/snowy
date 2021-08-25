package snowy.playfield

/** Used to keep track of playfield items, e.g. for the Grid */
trait PlayfieldTracker[A <: PlayfieldItem[A]] {
  def add(item: A): Unit
  def remove(item: A): Unit
}

/** some playfield position trackers that do nothing, e.g. for the client */
object PlayfieldTracker {

  def nullTracker[A <: PlayfieldItem[A]] = new PlayfieldTracker[A] {
    override def add(item: A) = {}
    override def remove(item: A) = {}
  }

  object ImplicitNullTrackers {
    implicit val nullSnowballTracker = nullTracker[Snowball]
    implicit val nullSledTracker     = nullTracker[Sled]
    implicit val nullTreeTracker     = nullTracker[Tree]
    implicit val nullPowerUpTracker  = nullTracker[PowerUp]
  }
}
