package snowy.playfield

import java.util.concurrent.atomic.AtomicInteger

/** id of a playfield item.
  * All ids are unique
  */
case class PlayId[A](val id: Int) extends AnyVal

object PlayId {
  type SledId    = PlayId[Sled]
  type BallId    = PlayId[Snowball]
  type TreeId    = PlayId[Tree]
  type PowerUpId = PlayId[PowerUp]

  def nextId[A <: PlayfieldItem[A]](): PlayId[A] = PlayId(idCounter.getAndIncrement())

  private val idCounter = new AtomicInteger()
}
