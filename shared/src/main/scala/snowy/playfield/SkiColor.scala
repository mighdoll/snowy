package snowy.playfield

case class Color(r: Int, g: Int, b: Int) {
  override def toString = s"rgb($r, $g, $b)"

  def *(p: Double) = Color((r * p).toInt, (g * p).toInt, (b * p).toInt)
}

sealed trait SkiColor {
  def color = Color(100, 100, 100)
}
case object BasicSkis extends SkiColor
case object GreenSkis extends SkiColor {
  override val color = Color(120, 201, 44)
}
case object RedSkis extends SkiColor {
  override val color = Color(241, 78, 84)
}
case object YellowSkis extends SkiColor {
  override val color = Color(237, 228, 52)
}
case object OrangeSkis extends SkiColor {
  override val color = Color(232, 159, 33)
}
object SkiColors {
  val allSkis = Seq(BasicSkis, GreenSkis, RedSkis, YellowSkis, OrangeSkis)
}