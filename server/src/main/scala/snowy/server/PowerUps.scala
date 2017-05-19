package snowy.server

import java.util.concurrent.ThreadLocalRandom
import scala.collection.mutable
import com.typesafe.scalalogging.StrictLogging
import snowy.playfield.{HealthPowerUp, Playfield, PowerUp}

object PowerUps {
  case class Replace(old: PowerUp, time: Long)

  /** order in smallest time first order */
  val replaceOrdering = new Ordering[Replace] {
    override def compare(x: Replace, y: Replace): Int = {
      x.time - y.time match {
        case d if d < 0 => -1
        case d if d > 0 => 1
        case _          => 0
      }
    }
  }
}

import PowerUps._

/** A managed collection of PowerUps on the playfield */
class PowerUps(protected val playfield: Playfield)
    extends GridItems[PowerUp] with StrictLogging {
  private val areaPerPowerUp = 600 * 600
  private val area           = playfield.size.x.toInt * playfield.size.y.toInt
  private val targetCount    = area / areaPerPowerUp
  private val replaces       = mutable.SortedSet[Replace]()(replaceOrdering)

  items ++= initialPowerUps()

  def refresh(gameTime: Long): Traversable[PowerUp] = {
    val ready        = replaces.takeWhile(_.time < gameTime)
    val replacements = ready.map(replace => newPowerUp(replace.old))
    if (replacements.nonEmpty) {
      logger.warn(s"new powerUps: $replacements")
    }

    ready.foreach(replaces.remove(_))
    this ++= replacements

    replacements.toSeq
  }

  override def remove(item: PowerUp): Unit = ??? // don't call this directly

  def removePowerUp(item: PowerUp, gameTime: Long): Unit = {
    logger.warn(s"removing PowerUp: $item")
    super.remove(item)
    scheduleReplacement(item, gameTime)
  }

  private def newPowerUp(old: PowerUp): PowerUp = {
    // for now, simply replace with same power up at the same spot
    // LATER make a potentially new kind of power up, at a nearby spot
    old
  }

  private def scheduleReplacement(item: PowerUp, gameTime: Long): Unit = {
    val maxDelaySeconds = 30
    val replaceTime = {
      val delay =
        (ThreadLocalRandom.current().nextDouble() * maxDelaySeconds * 1000).toInt
      logger.warn(s"delay for powerUp: $delay $item")
      gameTime + delay
    }
    replaces += Replace(item, replaceTime)
  }

  private def initialPowerUps(): Set[PowerUp] = {
    (1 to targetCount).map { _ =>
      val powerUp = new HealthPowerUp()
      powerUp.setInitialPosition(playfield.randomSpot())
    }.toSet
  }

}
