package snowy.server.rewards

object RevengeRewards {
  def rewards(): Seq[Reward] = {
    Seq(Score(100), MaxHealthBonus(1.5))
  }
}
