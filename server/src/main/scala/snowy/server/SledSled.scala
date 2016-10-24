package snowy.server

import scala.collection.mutable
import snowy.GameConstants.Collision.{maxSledCost, minSledCost}
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
    collideCircles(a, b).map { collided =>
      val injury = {
        val collisionSpeed = (a.speed - b.speed).length
        val damageFactor = math.min(1.0, collisionSpeed / absoluteMaxSpeed)
        minSledCost + (maxSledCost - minSledCost) * damageFactor
      }
      InjuredSled(collided, injury)
    }
  }

  /** A collided sled */
  private case class InjuredSled(collided: Collided[Sled], injury: Double) {
    def sled: Sled = collided.movingCircle
  }


}
