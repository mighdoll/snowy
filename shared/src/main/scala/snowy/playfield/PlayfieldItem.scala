package snowy.playfield
import vector.Vec2d

/** A playfield item in a mutable set mirrored to clients */
trait SharedItem

/** A mutable game object positionable on the playfield.
  *
  * The position of the object is tracked externally via a PlayfieldTracker
  * to enable updating of an external database of game objects, such as the
  * collision Grid.
  */
trait PlayfieldItem[A <: PlayfieldItem[A]] extends Bounds { this: A =>
  private var internalPosition = Vec2d.zero
  val id: PlayId[A]            = PlayId.nextId[A]()

  def position: Vec2d = internalPosition

  def position_=(pos: Vec2d)(implicit tracker: PlayfieldTracker[A]): Unit = {
    tracker.remove(this)
    internalPosition = pos
    tracker.add(this)
  }

  def setInitialPosition(pos: Vec2d)(implicit tracker: PlayfieldTracker[A]): A = {
    internalPosition = pos
    tracker.add(this)
    this
  }

  def canEqual(a: Any): Boolean = a.isInstanceOf[PlayfieldItem[A]]

  override def hashCode(): Int = id.id

  override def equals(that: Any): Boolean = that match {
    case that: PlayfieldItem[A] => canEqual(that) && id == that.id
    case _                      => false
  }

  /** increase collision impact on opponent by this factor */
  def impactDamage: Double = 1.0

  override def toString() = {
    val named = getClass.getSimpleName
    s"$named[${id.id}]"
  }
}

/** An object with a bounding box */
trait Bounds {
  def boundingBox: Rect
}
