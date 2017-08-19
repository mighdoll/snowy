package snowy.server.rewards

import scala.concurrent.duration.FiniteDuration
import com.typesafe.scalalogging.StrictLogging
import snowy.GameConstants.Points.minPoints
import snowy.GameConstants.{absoluteMaxHealth, absoluteMaxSpeed, kingHealthBonus}
import snowy.server.{ServerSled, User}
import snowy.util.ClosestTable

sealed trait RewardTableEntry

trait TableToReward {
  def toSingleReward(repeat: Int): Reward
}

case class SpeedTable(amounts: Int*) extends TableToReward with RewardTableEntry {
  private val table = new ClosestTable[Int](0, amounts: _*)

  override def toSingleReward(repeat: Int): Reward = {
    val speed = table.get(repeat)
    MaxSpeedBonus(speed)
  }
}

sealed trait Reward extends RewardTableEntry {
  def applyToSled(serverSled: ServerSled)
}

case class Score(amount: Int) extends Reward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    serverSled.user.score += amount
  }
}

case class MultiplyScore(fromUser: User, multiple: Double)
    extends Reward with StrictLogging {
  override def applyToSled(serverSled: ServerSled): Unit = {
    val proposedScore = serverSled.user.score + fromUser.score * multiple
    serverSled.user.score = math.max(minPoints, proposedScore)
    logger.info(
      s"AddScore to: ${serverSled.user}=${serverSled.user.score} "
        + s"from: ${fromUser}=${fromUser.score}  multiple: $multiple"
    )
  }
}

case class MaxSpeedBonus(amount: Int) extends Reward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    val current = serverSled.sled.maxSpeed
    serverSled.sled.maxSpeed = math.min(current + amount, absoluteMaxSpeed)
  }
}

case object FullHealth extends Reward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    serverSled.sled.health = serverSled.sled.maxHealth
  }
}

case class TemporarySpeed(amount: Int, duration: FiniteDuration) extends Reward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    serverSled.sled.maxSpeedBoost.start(amount, duration, serverSled.gameTime())
  }
}

case class MaxHealthBonus(amount: Double) extends Reward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    val current = serverSled.sled.maxHealth
    serverSled.sled.maxHealth = math.min(current + amount, absoluteMaxHealth)
  }
}

case object OnFire extends Reward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    serverSled.sled.maxHealth += kingHealthBonus
    serverSled.sled.health = serverSled.sled.maxHealth
  }
}

case object NoMoreFire extends Reward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    serverSled.sled.maxHealth -= kingHealthBonus
  }
}
