package snowy

import snowy.playfield.PlayId.{BallId, SledId}
import snowy.playfield._

/** messages sent to the server */
object GameServerProtocol {

  sealed trait GameServerMessage

  sealed trait StartStopControl

  sealed trait DriveControl extends StartStopControl

  sealed trait PersistentControl extends StartStopControl

  case class Join(userName: String,
                  sledKind: SledKind = BasicSled,
                  skiColor: SkiColor = BasicSkis)
      extends GameServerMessage

  case object ReJoin extends GameServerMessage

  case class TargetAngle(angle: Double) extends GameServerMessage

  case class Shoot(time: Long) extends GameServerMessage

  case class Boost(time: Long) extends GameServerMessage

  case class Start(cmd: StartStopControl, time: Long) extends GameServerMessage

  case class Stop(cmd: StartStopControl, time: Long) extends GameServerMessage

  case object Left extends PersistentControl

  case object Right extends PersistentControl

  case object Slowing extends DriveControl

  case object Coasting extends DriveControl

  case object Shooting extends PersistentControl

  case object Pong extends GameServerMessage

  case object ClientPing extends GameServerMessage

  case object TestDie extends GameServerMessage

  case class RequestGameTime(clientTime: Long) extends GameServerMessage

}

/** messages sent to the web client */
object GameClientProtocol {

  sealed abstract class GameClientMessage

  case class State(gameTime: Long, sleds: Seq[Sled], snowballs: Seq[Snowball])
      extends GameClientMessage

  case class MySled(id: SledId) extends GameClientMessage

  case class SledDeaths(sleds: Seq[SledId]) extends GameClientMessage

  case class SnowballDeaths(snowballs: Seq[BallId]) extends GameClientMessage

  case class PlayfieldBounds(width: Int, height: Int) extends GameClientMessage

  case class Trees(trees: Seq[Tree]) extends GameClientMessage

  case object Died extends GameClientMessage

  case class Score(userName: String, score: Double)

  case class Scoreboard(myScore: Double, scores: Seq[Score]) extends GameClientMessage

  case object Ping extends GameClientMessage

  case object ClientPong extends GameClientMessage

  case class GameTime(millis: Long, oneWayDelay: Int) extends GameClientMessage

}
