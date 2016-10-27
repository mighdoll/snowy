package snowy.server

import scala.collection.mutable
import snowy.GameConstants.Collision.minSledCost
import snowy.GameConstants.absoluteMaxSpeed
import snowy.collision.Collisions.{Collided, collideCircles}
import snowy.playfield.Sled

object SledSled {

  /** Check for collisions between all sleds.
    *
    * @return modified sleds for any sleds that have collided
    */
  def collide(sleds: Traversable[Sled]): Traversable[SledReplace] = {
    val damaged = damagedSleds(sleds)

    damaged.map { newSled =>
      val oldSled = sleds.find(_.id == newSled.id).get
      SledReplace(oldSled, newSled)
    }
  }

  /** Check for collisions between all sleds.
    *
    * @return modified sleds for any sleds that have collided
    */
  private def damagedSleds(sleds: Traversable[Sled]): Traversable[Sled] = {
    val damagedSleds = mutable.Set[Sled]()

    def applyDamage(injuredSled: InjuredSled): Unit = {
      import injuredSled.collided.{rebound, reposition}
      import injuredSled.{injury, sled}
      damagedSleds.add(sled.copy(
        health = sled.health - injury,
        pos = sled.pos + reposition,
        speed = sled.speed + rebound
      ))
    }

    val injuries: List[InjuredSled] = {
      sleds.toList.combinations(2).flatMap { elems =>
        elems match {
          case a :: b :: Nil => collideTwoSleds(a, b)
          case _             => ??? // can't happen AFAIK
        }
      }.toList
    }

    injuries foreach applyDamage
    damagedSleds
  }


  /** Collide two sleds and return the collision adjustments, or Nil */
  private def collideTwoSleds(a: Sled, b: Sled): List[InjuredSled] = {
    collideCircles(a, b) match {
      case collidedA :: collidedB :: Nil =>
        List(
          InjuredSled(collidedA, impactDamage(a, b)),
          InjuredSled(collidedB, impactDamage(b, a))
        )
      case Nil                           => Nil
      case _                             => ???
    }
  }

  /** @return the damage to sled a from a collision with sled b */
  private def impactDamage(a: Sled, b: Sled): Double = {
    val collisionSpeed = (a.speed - b.speed).length
    val speedFactor = math.min(1.0, collisionSpeed / absoluteMaxSpeed)
    val baseDamage = minSledCost + (b.maxImpactDamage - minSledCost) * speedFactor
    baseDamage / a.armor
  }

  /** A collided sled */
  private case class InjuredSled(collided: Collided[Sled], injury: Double) {
    def sled: Sled = collided.movingCircle
  }


}
