package snowy.server.rewards

import scala.collection.mutable
import com.typesafe.scalalogging.StrictLogging
import snowy.GameConstants.Points
import snowy.server.ServerSled
import snowy.server.rewards.Achievements._
import snowy.server.rewards.IcingStreakRewards.rewardsForStreak

/** Track achievements by a sled, and apply appropriate rewards to the sled/user */
class Rewards extends StrictLogging {
  private val achievementHistory = mutable.Map[Achievement, Int]()

  /** Record a new achievement by a sled, and apply appropriate rewards */
  def add(achievement: Achievement): Unit = {
    val already: Int = achievementHistory.getOrElse(achievement, 0)
    val rewards: Seq[(ServerSled, SingleReward)] =
      achievement match {
        case IcingStreak(serverSled, streak) =>
          rewardsForStreak(streak, already).map(serverSled -> _)
        case RevengeIcing(serverSled, _) =>
          RevengeRewards.rewards().map(serverSled -> _)
        case SledOut(serverSled) =>
          Seq(serverSled -> AddScoreFrom(serverSled.user, -Points.sledLoss))
        case SledIced(serverSled, icedServerSled) =>
          Seq(serverSled -> AddScoreFrom(icedServerSled.user, Points.sledKill))
      }

    for { (serverSled, reward) <- rewards } {
      logger.info(s"applying $reward to ${serverSled.user.name} ${serverSled.sled.id}")
      reward.applyToSled(serverSled)
    }

    achievementHistory(achievement) = already + 1
  }
}
