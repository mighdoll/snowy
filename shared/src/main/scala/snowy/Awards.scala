package snowy

import snowy.playfield.PlayId.SledId

object Awards {
  sealed trait Award

  case class SledKill(sledId: SledId, deadSled: SledId) extends Award

}
