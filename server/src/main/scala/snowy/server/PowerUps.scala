package snowy.server

import java.util.concurrent.ThreadLocalRandom

//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import snowy.playfield.{HealthPowerUp, Playfield, PowerUp, SpeedPowerUp}

import scala.collection.mutable

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

import snowy.server.PowerUps._

/** A managed collection of PowerUps on the playfield */
class PowerUps(protected val playfield: Playfield)
    extends GridItems[PowerUp] with Logging {
  private val areaPerPowerUp = 600 * 600
  private val area           = playfield.size.x.toInt * playfield.size.y.toInt
  private val targetCount    = area / areaPerPowerUp
  private val replaces       = mutable.SortedSet[Replace]()(replaceOrdering)

  items ++= initialPowerUps()

  def refresh(gameTime: Long): Traversable[PowerUp] = {
    val ready        = replaces.takeWhile(_.time < gameTime)
    val replacements = ready.map(replace => newPowerUp(replace.old))
    if (replacements.nonEmpty) {
      logger.info(s"new powerUps: $replacements")
    }

    ready.foreach(replaces.remove(_))
    this ++= replacements

    replacements.toSeq
  }

  case class IllegalCallException() extends RuntimeException

  /** don't call this directly, call removePowerUp instead */
  override def remove(item: PowerUp): Unit = throw IllegalCallException()

  def removePowerUp(item: PowerUp, gameTime: Long): Unit = {
    logger.info(s"removing PowerUp: $item")
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
      gameTime + delay
    }
    replaces += Replace(item, replaceTime)
  }

  private def initialPowerUps(): Set[PowerUp] = {
    (1 to targetCount).map { _ =>
      val powerUp = math.random match {
        case x if x < 0.5 => new HealthPowerUp()
        case x if x < 1.0 => new SpeedPowerUp()
      }
      powerUp.setInitialPosition(playfield.randomSpot())
    }.toSet
  }

}
