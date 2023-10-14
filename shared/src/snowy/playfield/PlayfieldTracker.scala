package snowy.playfield

import scala.reflect.ClassTag

/** Used to keep track of playfield items, e.g. for the Grid */
trait PlayfieldTracker[A <: PlayfieldItem[A]] {
  def add(item: A): Unit
  def remove(item: A)(implicit ct: ClassTag[A]): Unit
}

/** some playfield position trackers that do nothing, e.g. for the client */
object PlayfieldTracker {

  def nullTracker[A <: PlayfieldItem[A]] = new PlayfieldTracker[A] {
    override def add(item: A)                              = {}
    override def remove(item: A)(implicit ct: ClassTag[A]) = {}
  }

  object ImplicitNullTrackers {
    implicit val nullSnowballTracker: PlayfieldTracker[Snowball] = nullTracker[Snowball]
    implicit val nullSledTracker: PlayfieldTracker[Sled]         = nullTracker[Sled]
    implicit val nullTreeTracker: PlayfieldTracker[Tree]         = nullTracker[Tree]
    implicit val nullPowerUpTracker: PlayfieldTracker[PowerUp]   = nullTracker[PowerUp]
  }
}
