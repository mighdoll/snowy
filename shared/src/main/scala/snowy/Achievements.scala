package snowy

import snowy.playfield.PlayId.SledId

object Achievements {
  sealed trait Achievement

  case class IceStreak(sledId: SledId, amount: Int) extends Achievement

  case class RevengeIcing(mySledId: SledId, otherUserName: String) extends Achievement
}
