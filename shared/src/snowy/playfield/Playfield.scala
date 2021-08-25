package snowy.playfield

import java.util.concurrent.ThreadLocalRandom

import snowy.playfield.Playfield._
import vector.Vec2d

object Playfield {

  /** Constrain a value between 0 and a max value.
    * values past one border of the range are wrapped to the other side
    *
    * @return the wrapped value
    */
  def wrapBorder(value: Double, max: Double): Double = {
    val result =
      if (value >= max * 2.0)
        max
      else if (value >= max)
        value - max
      else if (value < -max)
        0
      else if (value < 0)
        max + value
      else
        value
    if (result < 0 || result > max) {
      println(s"wrapBorder error: wrap $value  between (0 < $max) = $result")
    }
    result
  }
}

class Playfield(val size: Vec2d) {

  /** constrain a position to be within the playfield */
  def wrapInPlayfield(pos: Vec2d): Vec2d = {
    Vec2d(
      wrapBorder(pos.x, size.x),
      wrapBorder(pos.y, size.y)
    )
  }

  /** pick a random spot on the playfield */
  def randomSpot(): Vec2d = {
    val random = ThreadLocalRandom.current
    Vec2d(
      random.nextInt(size.x.toInt),
      random.nextInt(size.y.toInt)
    )
  }
}
