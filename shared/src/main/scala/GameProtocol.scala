
/** messages sent to the server */
object GameServerProtocol {

  sealed trait GameServerMessage

  sealed trait StartStopCommand

  case class Join(userName: String) extends GameServerMessage

  case class TurretAngle(angle: Double) extends GameServerMessage

  case object Shoot extends GameServerMessage

  case class Start(cmd: StartStopCommand) extends GameServerMessage

  case class Stop(cmd: StartStopCommand) extends GameServerMessage

  case object Left extends StartStopCommand

  case object Right extends StartStopCommand

  case object Slow extends StartStopCommand

  case object Push extends StartStopCommand

}

/** messages sent to the web client */
object GameClientProtocol {

  case class Position(x: Int, y: Int)

  /** rotations in radians, 0 is down */
  case class Sled(userName: String, position: Position,
                  rotation: Double, turretRotation: Double,
                  health: Double, pushEnergy: Double)

  case class Tree(size: Int, position: Position)

  case class Snowball(size: Int, position: Position)

  sealed abstract class GameClientMessage

  case class State(mySled: Sled, sleds: Seq[Sled], snowballs: Seq[Snowball]) extends GameClientMessage

  case class PlayField(width: Int, height: Int) extends GameClientMessage

  case class Trees(trees: Seq[Tree]) extends GameClientMessage

}
