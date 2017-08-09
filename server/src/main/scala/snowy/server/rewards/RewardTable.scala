package snowy.server.rewards

import snowy.server.ServerSled
import snowy.util.ClosestTable
import snowy.GameConstants.absoluteMaxSpeed
import snowy.GameConstants.absoluteMaxHealth

sealed trait RewardTableEntry

trait TableToReward {
  def toSingleReward(repeat: Int): SingleReward
}

case class SpeedTable(amounts: Int*) extends TableToReward with RewardTableEntry {
  private val table = new ClosestTable[Int](0, amounts: _*)

  override def toSingleReward(repeat: Int): SingleReward = {
    val speed = table.get(repeat)
    MaxSpeedBonus(speed)
  }
}

trait SingleReward extends RewardTableEntry {
  def applyToSled(serverSled: ServerSled)
}

case class Score(amount: Int) extends SingleReward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    serverSled.user.score += amount
  }
}

case object HalveScore extends SingleReward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    serverSled.user.score /= 2
  }
}

case class MaxSpeedBonus(amount: Int) extends SingleReward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    val current = serverSled.sled.maxSpeed
    serverSled.sled.maxSpeed = math.min(current + amount, absoluteMaxSpeed)
  }
}

case class MaxHealthBonus(amount: Double) extends SingleReward {
  override def applyToSled(serverSled: ServerSled): Unit = {
    val current = serverSled.sled.maxHealth
    serverSled.sled.maxHealth = math.min(current + amount, absoluteMaxHealth)
  }
}
