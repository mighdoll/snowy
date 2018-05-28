package snowy.collision

import cats._
import snowy.GameConstants.absoluteMaxSpeed
import snowy.collision.Collisions.{collideCircles, Collided}
import snowy.playfield._
import vector.Vec2d
import scala.collection.mutable.ListBuffer
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging

object CollideThings {

  /** Collide two sets of circular objects with each other
    * The objects speeds, positions, and health are modified based on the collision.
    *
    * @return a list of any killed objects */
  def collideWithGrid[A <: MovableCircularItem[A]: PlayfieldTracker, B <: MovableCircularItem[
    B
  ]: PlayfieldTracker](
        aCollection: Traversable[A],
        bGrid: Grid[B]
  ): DeathList[A, B] = {

    val itemPairs: Traversable[(A, B)] =
      for {
        a <- aCollection
        b <- bGrid.inside(a.boundingBox)
      } yield {
        (a, b)
      }

    val effects =
      for {
        (a, b)             <- itemPairs
        (effectA, effectB) <- collideCircular(a, b)
      } yield {
        (effectA, effectB)
      }

    // apply the collisions to the items and return any killed items
    val deaths: Traversable[DeathList[A, B]] =
      for { (effectA, effectB) <- effects } yield applyTwoEffects(effectA, effectB)

    Monoid.combineAll(deaths)
  }

  /** Collide all elements in a collection of circular objects with each other
    * The objects speeds, positions, and health are modified based on the collision.
    *
    * @return a list of any killed objects */
  def collideCollection[A <: MovableCircularItem[A]: PlayfieldTracker](
        collection: Traversable[A],
        grid: Grid[A]
  ): Traversable[Death[A, A]] = {
    val pairs = for {
      item     <- collection
      neighbor <- grid.inside(item.boundingBox)
      if neighbor != item
    } yield {
      Set(item, neighbor)
    }
    val uniquePairs = pairs.toSet

    val effectPairs: Seq[(CollisionEffect[A], CollisionEffect[A])] =
      for {
        pairSet <- uniquePairs.toList
        Seq(a, b) = pairSet.toSeq
        collisions <- collideCircular(a, b)
      } yield {
        collisions
      }

    val deathLists: Iterable[DeathList[A, A]] = effectPairs.map {
      case (effectA, effectB) =>
        applyTwoEffects(effectA, effectB)
    }

    val deaths: Iterable[Death[A, A]] = deathLists.flatMap { deathList =>
      deathList.a ++ deathList.b
    }

    deaths.toSeq
  }

  /** Modify two objects with the effects of a collision.
    * (the effects are deferred until all collisions are calculated) */
  private def applyTwoEffects[A <: MovableCircularItem[A]: PlayfieldTracker, B <: MovableCircularItem[
    B
  ]: PlayfieldTracker](
        effectA: CollisionEffect[A],
        effectB: CollisionEffect[B]
  ): DeathList[A, B] = {
    effectA.applyEffects()
    effectB.applyEffects()
    val a       = effectA.collided.item
    val b       = effectB.collided.item
    val aDeaths = ListBuffer[Death[A, B]]()
    val bDeaths = ListBuffer[Death[B, A]]()
    if (a.health <= 0) {
      aDeaths append Death(a, b)
    }
    if (b.health <= 0) {
      bDeaths append Death(b, a)
    }
    DeathList(aDeaths, bDeaths)
  }

  private def collideCircular[A <: MovableCircularItem[A], B <: MovableCircularItem[B]](
        objA: A,
        objB: B
  ): Option[(CollisionEffect[A], CollisionEffect[B])] = {
    collideCircles(objA, objB) map {
      case (aCollision, bCollision) =>
        val aDamage = CollisionEffect(aCollision, impactDamage(objA, objB))
        val bDamage = CollisionEffect(bCollision, impactDamage(objB, objA))
        (aDamage, bDamage)
    }
  }

  /** @return the damage to sled a from a collision with sled b */
  private def impactDamage[A <: MovableCircularItem[A], B <: MovableCircularItem[B]](
        objA: A,
        objB: B
  ): Double = {
    val collisionSpeed = (objA.speed - objB.speed).length
    val speedFactor    = math.min(1.0, collisionSpeed / absoluteMaxSpeed)
    objB.impactDamage * speedFactor * objB.mass
  }
}

/** Pending changes to an objects health,speed, etc. after a collision */
case class CollisionEffect[A <: MovableCircularItem[A]](collided: Collided[A],
                                                        damage: Double)
    extends Logging {
  def applyEffects()(implicit tracker: PlayfieldTracker[A]): Unit = {
    val obj = collided.item
    obj.health = obj.health - damage
    logger.trace(s"applyEffects on $obj  health:${obj.health}")
    if (obj.health > 0) { // don't bother changing position or speed if its dead anyway
      obj.speed = obj.speed + collided.rebound
      obj.position = obj.position + collided.reposition
    }
  }

  override def toString: String = {
    def vecToString(vec: Vec2d): String = {
      s"${vec.x.toInt}, ${vec.y.toInt}"
    }
    collided.item match {
      case item: PlayfieldItem[_] =>
        val named  = item.getClass.getSimpleName
        val pos    = vecToString(item.position)
        val bboxTL = vecToString(item.boundingBox.pos)
        val bboxBR = vecToString(item.boundingBox.pos + item.boundingBox.size)
        s"CollisionEffect: $named[${item.id.id}]   pos: $pos   bbox: $bboxTL to $bboxBR"
      case x => s"CollisionEffect on $x"
    }
  }
}

/** A report that one of the objects in a collision has run out of health */
case class Death[A <: MovableCircularItem[A], B <: MovableCircularItem[B]](killed: A,
                                                                           killer: B)

/** a report of one or more objects that have been killed */
case class DeathList[A <: MovableCircularItem[A], B <: MovableCircularItem[B]](
      a: Traversable[Death[A, B]],
      b: Traversable[Death[B, A]]
)

object DeathList {
  implicit def deathListMonoid[A <: MovableCircularItem[A], B <: MovableCircularItem[B]]
    : Monoid[DeathList[A, B]] = {
    new Monoid[DeathList[A, B]] {
      def empty = DeathList[A, B](Nil, Nil)

      def combine(x: DeathList[A, B], y: DeathList[A, B]): DeathList[A, B] = {
        DeathList(x.a ++ y.a, x.b ++ y.b)
      }
    }
  }
}
