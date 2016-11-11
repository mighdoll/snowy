package snowy.collision

import scala.collection.mutable.ListBuffer
import snowy.GameConstants.absoluteMaxSpeed
import snowy.collision.Collisions.{collideCircles, Collided}
import snowy.playfield.CircularObject

object CollideThings {

  /** Collide two sets of circular objects with each other
    * The objects speeds, positions, and health are modified based on the collision.
    *
    * @return a list of any killed objects */
  def collideThings[A <: CircularObject, B <: CircularObject](
        aCollection: Traversable[A],
        bCollection: Traversable[B]): Traversable[Death[_, _]] = {

    val allDeaths =
      for {
        objA               <- aCollection
        objB               <- bCollection
        (effectA, effectB) <- collide2(objA, objB)
      } yield {
        applyTwoEffects(effectA, effectB)
      }
    allDeaths.flatten
  }

  /** Collide all elements in a collection of circular objects with each other
    * The objects speeds, positions, and health are modified based on the collision.
    *
    * @return a list of any killed objects */
  def collideCollection[A <: CircularObject](
        collection: Traversable[A]): Traversable[Death[_, _]] = {
    val combinations = collection.toList.combinations(2)
    val effectPairs = combinations.flatMap {
      case List(a, b) => collide2(a, b)
    }
    val deaths = effectPairs.flatMap {
      case (effectA, effectB) =>
        applyTwoEffects(effectA, effectB)
    }

    deaths.toList
  }

  /** Modify two objects with the effects of a collision.
    * (the effects are deferred until all collisions are calculated) */
  private def applyTwoEffects[A <: CircularObject, B <: CircularObject](
        effectA: CollisionEffect[A],
        effectB: CollisionEffect[B]): List[Death[_, _]] = {
    effectA.applyEffects()
    effectB.applyEffects()
    val a      = effectA.collided.movingCircle
    val b      = effectB.collided.movingCircle
    val deaths = ListBuffer[Death[_, _]]()
    if (a.health <= 0) {
      deaths append Death(a, b)
    }
    if (b.health <= 0) {
      deaths append Death(b, a)
    }
    deaths.toList
  }

  private def collide2[A <: CircularObject, B <: CircularObject](
        objA: A,
        objB: B): Option[(CollisionEffect[A], CollisionEffect[B])] = {
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
