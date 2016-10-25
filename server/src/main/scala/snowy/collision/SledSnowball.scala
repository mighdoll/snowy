package snowy.collision

import snowy.playfield.{Sled, Snowball, Store}
import snowy.Awards.SnowballHit

object SledSnowball {
  /** Intersect the sled with all potentially overlapping snowballs on the playfield.
    * If a snowball collides with the sled, remove the snowball from the playfield
    *
    * @return a damaged sled if it intersects with a snowball */
  def collide(sled: Sled, snowballs: Store[Snowball])
      : Option[(Sled, Store[Snowball], Traversable[SnowballHit])] = {
    val collisions =
      snowballs.items.filter { snowball =>
        snowball.ownerId != sled.id && snowballCollide(sled, snowball)
      }

    val newBalls = collisions.foldLeft(snowballs) { (balls, snowball) =>
      balls.remove(snowball)
    }
    val scores = collisions.foldLeft(List[SnowballHit]()) { (list, snowball) =>
      SnowballHit(snowball.ownerId) :: list
    }

    collisions.headOption.map { snowball =>
      (snowballDamaged(sled, snowball), newBalls, scores)
    }
  }

  /** return true if the snowball intersects the sled body */
  private def snowballCollide(sled: Sled, snowball: Snowball): Boolean = {
    val centerDistance = (sled.pos - snowball.pos).length
    val radiiSum = sled.size / 2 + snowball.size / 2
    centerDistance < radiiSum
  }

  /** return a damaged version of the sled after impacting with a snowball */
  private def snowballDamaged(sled: Sled, snowball: Snowball): Sled = {
    val health = math.max(sled.health - snowball.power, 0)
    val stopped = sled.speed + snowball.speed * 15
    sled.copy(health = health, speed = stopped)
  }
}
