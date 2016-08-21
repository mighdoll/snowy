import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import cats.data.Xor
import scala.reflect.ClassTag

trait GameCommon[T] {
  val decoders: Iterable[String => Xor[Error, T]]

  def decodeMessage(text: String): Option[T] = {
    decoders
      .map { decoder => decoder(text).toOption }
      .collectFirst { case Some(proto) => proto }
  }
}

/** messages sent to the server */
object GameServerMessages {
  // TODO messageType isn't in the json yet
  sealed abstract class GameServerProtocol {
    def encode:String = {
      ???
    }
  }
  case class Join(userName: String) extends GameServerProtocol
  case class Move(x: Int, y: Int) extends GameServerProtocol

  object Protocol extends GameCommon[GameServerProtocol] {
    val decoders = Seq(decode[Join] _, decode[Move] _)
  }
}

/** messages sent to the web client */
object GameClientMessages {
  case class Position(x: Int, y: Int)
  case class Snake(user: User, position: Position)
  case class User(id: String)

  sealed trait GameClientProtocol
  case class State(users: Seq[Snake]) extends GameClientProtocol

  object ClientProtocol extends GameCommon[GameClientProtocol] {
    val decoders = Seq(decode[State] _)
  }
}
