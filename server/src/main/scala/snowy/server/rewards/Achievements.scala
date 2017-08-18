package snowy.server.rewards

import snowy.server.ServerSled

/** Rewardable accomplishments by the user in a game */
object Achievements {
  sealed trait Achievement {
    def sled: ServerSled
  }

  /** Ice a series of other sleds within a short time */
  case class IcingStreak(override val sled: ServerSled, amount: Int) extends Achievement

  /** Ice the user that iced you previously */
  case class RevengeIcing(override val sled: ServerSled, otherUserName: String)
      extends Achievement

  /** Sled was iced (knocked out). Kind of a negative achievement */
  case class SledOut(override val sled: ServerSled) extends Achievement

  /** Sled iced (knocked out) another sled. */
  case class SledIced(override val sled: ServerSled, icedSled: ServerSled)
      extends Achievement

  /** A new sled tops the score leaderboard */
  case class Kinged(override val sled: ServerSled, oldKing: Option[ServerSled])
      extends Achievement

  /** Achieved a notable total number of icings */
  case class IceTotal(override val sled: ServerSled, total: Int) extends Achievement

  /** Collected a power up on the playfield */
  case class PowerUpCollected(override val sled: ServerSled, reward: Reward) extends Achievement
}

/*
Adding a new Achievement:
  . Add the achievement here
  . Produce the achievement in PlayfieldSteps
  . Apply effects of the achievement on the sled in Rewards
  . Notify the client about the reward in ClientReporting
 */
