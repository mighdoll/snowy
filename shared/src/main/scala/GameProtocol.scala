
/** messages sent to the server */
object GameServerProtocol {
  sealed trait GameServerMessage
  case class Join(userName: String) extends GameServerMessage
  case object TurnLeft extends GameServerMessage
  case object Turnright extends GameServerMessage
}

/** messages sent to the web client */
object GameClientProtocol {
  case class Position(x: Int, y: Int)
  case class Sled(user: User, position: Position)
  case class User(name: String)
  case class Tree(size:Int, position:Position)
  case class Snowball(size:Int, color:String, position:Position)

  sealed abstract class GameClientMessage
  case class State(sleds: Seq[Sled], snowballs:Seq[Snowball]) extends GameClientMessage
  case class PlayField(height:Int, width:Int) extends GameClientMessage
  case class Trees(trees:Seq[Tree]) extends GameClientMessage
}
