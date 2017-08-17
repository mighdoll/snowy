package snowy.server.rewards

import snowy.GameConstants.Points
import snowy.server.ServerSled
import snowy.server.rewards.Achievements.SledIced

object IceRewards {
  def rewards(sledIced: SledIced): Seq[(ServerSled, SingleReward)] = {
    val SledIced(serverSled, icedServerSled) = sledIced
    // half the points from the other sled
    Seq(serverSled -> MultiplyScore(icedServerSled.user, Points.sledKill))
  }
}
