package snowy.server.rewards

import snowy.util.ClosestTable

object IcingStreakRewards {
  private val rewardsByStreak = new ClosestTable[Seq[RewardTableEntry]](
    1,
    Seq(),
    Seq(Score(20), SpeedTable(20, 10, 5)),
    Seq(Score(40), SpeedTable(20, 10, 5)),
    Seq(Score(60), SpeedTable(20, 10, 5))
  )

  def rewardsForStreak(streak: Int, repeat: Int): Seq[Reward] = {
    rewardsByStreak.get(streak).map {
      case s: Reward         => s
      case table: SpeedTable => table.toSingleReward(repeat)
    }
  }
}
