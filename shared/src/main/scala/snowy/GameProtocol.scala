package snowy

import snowy.playfield._

/** messages sent to the server */
object GameServerProtocol {

  sealed trait GameServerMessage

  sealed trait StartStopCommand

  case class Join(userName: String, sledKind: SledKind = BasicSled, skiColor: SkiColor = BasicSkis)
      extends GameServerMessage

  case object ReJoin extends GameServerMessage

  case class TurretAngle(angle: Double) extends GameServerMessage

  case class Shoot(time: Long) extends GameServerMessage

  case class Push(time: Long) extends GameServerMessage

  case class Start(cmd: StartStopCommand, time: Long) extends GameServerMessage

  case class Stop(cmd: StartStopCommand, time: Long) extends GameServerMessage

  case object Left extends StartStopCommand

  case object Right extends StartStopCommand

  case object Slowing extends StartStopCommand

  case object TurretLeft extends StartStopCommand

  case object TurretRight extends StartStopCommand

  case object Shooting extends StartStopCommand

  case object Pong extends GameServerMessage

  case object TestDie extends GameServerMessage

  case class RequestGameTime(clientTime: Long) extends GameServerMessage

}

/** messages sent to the web client */
object GameClientProtocol {

  sealed abstract class GameClientMessage

  case class State(gameTime: Long,
                   mySled: Sled,
                   sleds: Seq[Sled],
                   snowballs: Seq[Snowball])
      extends GameClientMessage

  case class Playfield(width: Int, height: Int) extends GameClientMessage

  case class Trees(trees: Seq[Tree]) extends GameClientMessage

  case object Died extends GameClientMessage

  case class Score(userName: String, score: Double)

  case class Scoreboard(myScore: Double, scores: Seq[Score]) extends GameClientMessage

  case object Ping extends GameClientMessage

  case class GameTime(millis: Long, oneWayDelay: Int) extends GameClientMessage

}
