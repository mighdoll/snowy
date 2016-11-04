package snowy

import snowy.playfield.PlayId.SledId

object Awards {
  sealed trait Award

  case class SnowballHit(sledId: SledId) extends Award

  case class SledKill(sledId: SledId, deadSled: SledId) extends Award

  case class Travel(sledId: SledId, distance: Double) extends Award

  case class SledDied(sledId: SledId) extends Award
}
