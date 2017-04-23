package snowy.collision

import scala.collection.mutable.ListBuffer
import snowy.GameConstants.absoluteMaxSpeed
import snowy.collision.Collisions.{collideCircles, Collided}
import snowy.playfield.{CircularObject, Grid, PlayfieldTracker}
import cats._

object CollideThings {

  /** Collide two sets of circular objects with each other
    * The objects speeds, positions, and health are modified based on the collision.
    *
    * @return a list of any killed objects */
  def collideThings2[A <: CircularObject[A]: PlayfieldTracker, B <: CircularObject[B]: PlayfieldTracker](
        aCollection: Traversable[A],
        bCollection: Traversable[B],
        aGrid: Grid[A],
        bGrid: Grid[B]
  ): DeathList[A, B] = {

    val pairsA: Traversable[(A, B)] =
      for {
        a <- aCollection
        b <- bGrid.inside(a.boundingBox)
      } yield {
        (a, b)
      }
    val pairsB: Traversable[(A, B)] =
      for {
        b <- bCollection
        a <- aGrid.inside(b.boundingBox)
      } yield {
        (a, b)
      }

    // CONSIDER if the grid was perfect (re:wrapping, etc.) pairsA should equal pairsB
    val uniquePairs = pairsA.toSet // ++ pairsB.toSet

    val deaths =
      for {
        (a, b)             <- uniquePairs
        (effectA, effectB) <- collide2(a, b)
      } yield {
        applyTwoEffects(effectA, effectB)
      }

    Monoid.combineAll(deaths)
  }

  /** Collide two sets of circular objects with each other
    * The objects speeds, positions, and health are modified based on the collision.
    *
    * @return a list of any killed objects */
  def collideThings[A <: CircularObject[A]: PlayfieldTracker, B <: CircularObject[B]: PlayfieldTracker](
        aCollection: Traversable[A],
        bCollection: Traversable[B]
  ): DeathList[A, B] = {

    val deaths =
      for {
        objA               <- aCollection
        objB               <- bCollection
        (effectA, effectB) <- collide2(objA, objB)
      } yield {
        applyTwoEffects(effectA, effectB)
      }

    Monoid.combineAll(deaths)
  }

  /** Collide all elements in a collection of circular objects with each other
    * The objects speeds, positions, and health are modified based on the collision.
    *
    * @return a list of any killed objects */
  def collideCollection[A <: CircularObject[A]: PlayfieldTracker](
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
        collisions <- collide2(a, b)
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
  private def applyTwoEffects[A <: CircularObject[A]: PlayfieldTracker, B <: CircularObject[
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

  private def collide2[A <: CircularObject[A], B <: CircularObject[B]](
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
  private def impactDamage[A <: CircularObject[A], B <: CircularObject[B]](
        objA: A,
        objB: B
  ): Double = {
    val collisionSpeed = (objA.speed - objB.speed).length
    val speedFactor    = math.min(1.0, collisionSpeed / absoluteMaxSpeed)
    val baseDamage     = objB.impactDamage * speedFactor * objB.mass
    baseDamage / objA.armor
  }
}

/** Pending changes to an objects health,speed, etc. after a collision */
case class CollisionEffect[A <: CircularObject[A]](collided: Collided[A], damage: Double) {
  def applyEffects()(implicit tracker: PlayfieldTracker[A]): Unit = {
    val obj = collided.item
    obj.health = obj.health - damage
    obj.speed = obj.speed + collided.rebound
    obj.position = obj.position + collided.reposition
  }
}

/** A report that one of the objects in a collision has run out of health */
case class Death[A <: CircularObject[A], B <: CircularObject[B]](killed: A, killer: B)

/** a report of one or more objects that have been killed */
case class DeathList[A <: CircularObject[A], B <: CircularObject[B]](
      a: Traversable[Death[A, B]],
      b: Traversable[Death[B, A]]
)

object DeathList {
  implicit def deathListMonoid[A <: CircularObject[A], B <: CircularObject[B]]
    : Monoid[DeathList[A, B]] = {
    new Monoid[DeathList[A, B]] {
      def empty = DeathList[A, B](Nil, Nil)

      def combine(x: DeathList[A, B], y: DeathList[A, B]): DeathList[A, B] = {
        DeathList(x.a ++ y.a, x.b ++ y.b)
      }
    }
  }
}
