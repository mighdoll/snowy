
/** messages sent to the server */
object GameServerProtocol {
  sealed trait GameServerMessage
  case class Join(userName: String) extends GameServerMessage
  case class Move(x: Int, y: Int) extends GameServerMessage
}

/** messages sent to the web client */
object GameClientProtocol {
  case class Position(x: Int, y: Int)
  case class Sled(user: User, position: Position)
  case class User(id: String)

  sealed abstract class GameClientMessage
  case class State(sleds: Seq[Sled]) extends GameClientMessage
}
