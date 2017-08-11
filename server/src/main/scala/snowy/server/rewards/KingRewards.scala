package snowy.server.rewards

import snowy.server.ServerSled
import snowy.server.rewards.Achievements.Kinged

object KingRewards {
  def rewards(kinged: Kinged): Seq[(ServerSled, SingleReward)] = {

    val kingRewards = Seq(kinged.sled -> Score(100), kinged.sled -> OnFire)
    val oldKingReward = kinged.oldKing.map(_ -> NoMoreFire)

    kingRewards ++ oldKingReward
  }
}
