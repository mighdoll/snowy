package snowy.collision

import scala.collection.mutable.ListBuffer
import snowy.GameConstants.absoluteMaxSpeed
import snowy.collision.Collisions.{collideCircles, Collided}
import snowy.playfield.CircularObject
import cats._

object CollideThings {

  /** Collide two sets of circular objects with each other
    * The objects speeds, positions, and health are modified based on the collision.
    *
    * @return a list of any killed objects */
  def collideThings[A <: CircularObject, B <: CircularObject](
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
  def collideCollection[A <: CircularObject](
        collection: Traversable[A]
  ): Traversable[Death[A, A]] = {
    val combinations = collection.toList.combinations(2)
    val effectPairs: Iterator[(CollisionEffect[A], CollisionEffect[A])] =
      combinations.flatMap {
        case List(a, b) => collide2(a, b)
      }

    val deathLists: Iterator[DeathList[A, A]] = effectPairs.map {
      case (effectA, effectB) =>
        applyTwoEffects(effectA, effectB)
    }

    val deaths: Iterator[Death[A, A]] = deathLists.flatMap { deathList =>
      deathList.a ++ deathList.b
    }

    deaths.toSeq
  }

  /** Modify two objects with the effects of a collision.
    * (the effects are deferred until all collisions are calculated) */
  private def applyTwoEffects[A <: CircularObject, B <: CircularObject](
        effectA: CollisionEffect[A],
        effectB: CollisionEffect[B]
  ): DeathList[A, B] = {
    effectA.applyEffects()
    effectB.applyEffects()
    val a       = effectA.collided.movingCircle
    val b       = effectB.collided.movingCircle
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

  private def collide2[A <: CircularObject, B <: CircularObject](
        objA: A,
        objB: B
  ): Option[(CollisionEffect[A], CollisionEffect[B])] = {
    collideCircles(objA, objB) match {
      case Some((aCollision, bCollision)) =>
        val aDamage = CollisionEffect(aCollision, impactDamage(objA, objB))
        val bDamage = CollisionEffect(bCollision, impactDamage(objB, objA))
        Some((aDamage, bDamage))
      case None => None
    }
  }

  /** @return the damage to sled a from a collision with sled b */
  private def impactDamage[A <: CircularObject, B <: CircularObject](objA: A,
                                                                     objB: B): Double = {
    val collisionSpeed = (objA.speed - objB.speed).length
    val speedFactor    = math.min(1.0, collisionSpeed / absoluteMaxSpeed)
    val baseDamage     = objB.impactDamage * speedFactor * objB.mass
    baseDamage / objA.armor
  }
}

/** Pending changes to an objects health,speed, etc. after a collision */
case class CollisionEffect[A <: CircularObject](collided: Collided[A], damage: Double) {
  def applyEffects(): Unit = {
    val obj = collided.movingCircle
    obj.health = obj.health - damage
    obj.speed = obj.speed + collided.rebound
    obj.updatePos(obj.pos + collided.reposition)
  }
}

/** A report that one of the objects in a collision has run out of health */
case class Death[A <: CircularObject, B <: CircularObject](killed: A, killer: B)

/** a report of one or more objects that have been killed */
case class DeathList[A <: CircularObject, B <: CircularObject](
      a: Traversable[Death[A, B]],
      b: Traversable[Death[B, A]]
)

object DeathList {
  implicit def deathListMonoid[A <: CircularObject, B <: CircularObject]
    : Monoid[DeathList[A, B]] = {
    new Monoid[DeathList[A, B]] {
      def empty = DeathList[A, B](Nil, Nil)

      def combine(x: DeathList[A, B], y: DeathList[A, B]): DeathList[A, B] = {
        DeathList(x.a ++ y.a, x.b ++ y.b)
      }
    }
  }
}
