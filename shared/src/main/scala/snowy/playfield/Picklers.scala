package snowy.playfield
import boopickle.Default._

object Picklers {

  implicit val sledIdPickler = playIdPickler[Sled]
  implicit val ballIdPickler = playIdPickler[Snowball]
  implicit val TreeIdPickler = playIdPickler[Tree]

  private def playIdPickler[A]: Pickler[PlayId[A]] =
    transformPickler[PlayId[A], Int] { (id: Int) =>
      new PlayId[A](id)
    } {
      _.id
    }
}
