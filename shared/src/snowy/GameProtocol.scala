package snowy

import snowy.playfield.*
import snowy.playfield.PlayId.SledId
import upickle.default.{macroRW, readwriter, ReadWriter}
import upickle.default.ReadWriter.join

import scala.reflect.ClassTag

trait PlayIdTag[A] {
  def tag: String
  def construct(id: Int): PlayId[A]
}

object PlayIdTag {
  given PlayIdTag[Sled] with {
    def tag                = "sled"
    def construct(id: Int) = PlayId[Sled](id)
  }
  given PlayIdTag[Snowball] with {
    def tag                = "ball"
    def construct(id: Int) = PlayId[Snowball](id)
  }
  given PlayIdTag[Tree] with {
    def tag                = "tree"
    def construct(id: Int) = PlayId[Tree](id)
  }
  given PlayIdTag[PowerUp] with {
    def tag                = "powerup"
    def construct(id: Int) = PlayId[PowerUp](id)
  }

}

given playIdRW[A](using tagInst: PlayIdTag[A]): ReadWriter[PlayId[A]] = {
  readwriter[(String, Int)].bimap[PlayId[A]](
    pid => (tagInst.tag, pid.id),
    { case (tag, id) =>
      tag match {
        case "sled" =>
          PlayIdTag.given_PlayIdTag_Sled.construct(id).asInstanceOf[PlayId[A]]
        case "ball" =>
          PlayIdTag.given_PlayIdTag_Snowball.construct(id).asInstanceOf[PlayId[A]]
        case "tree" =>
          PlayIdTag.given_PlayIdTag_Tree.construct(id).asInstanceOf[PlayId[A]]
        case "powerup" =>
          PlayIdTag.given_PlayIdTag_PowerUp.construct(id).asInstanceOf[PlayId[A]]
        case _ => PlayId[A](id)
      }
    }
  )
}

/** messages sent to the server */
object GameServerProtocol {

  sealed trait GameServerMessage derives ReadWriter

  sealed trait StartStopControl derives ReadWriter

  sealed trait DriveControl extends StartStopControl derives ReadWriter

  sealed trait PersistentControl extends StartStopControl derives ReadWriter

  case class Join(
        userName: String,
        sledType: SledType = BasicSledType,
        skiColor: SkiColor = BasicSkis
  ) extends GameServerMessage

  case object ReJoin extends GameServerMessage

  case class TargetAngle(angle: Double) extends GameServerMessage

  case class Shoot(time: Long) extends GameServerMessage

  case class Boost(time: Long) extends GameServerMessage

  case class Start(cmd: StartStopControl, time: Long) extends GameServerMessage

  case class Stop(cmd: StartStopControl, time: Long) extends GameServerMessage

  case object Left extends PersistentControl

  case object Right extends PersistentControl

  case object Slowing extends DriveControl

  case object Shooting extends PersistentControl

  case object Pong extends GameServerMessage

  case object ClientPing extends GameServerMessage

  case object TestDie extends GameServerMessage

  case class DebugKey(key: Char) extends GameServerMessage

  case class RequestGameTime(clientTime: Long) extends GameServerMessage

}

/** messages sent to the web client */
object GameClientProtocol {

  sealed abstract class GameClientMessage derives ReadWriter

  case class State(gameTime: Long, sleds: Seq[Sled], snowballs: Seq[Snowball])
      extends GameClientMessage

  case class MySled(id: SledId) extends GameClientMessage

  case class PlayfieldBounds(width: Int, height: Int) extends GameClientMessage

  case class InitialTrees(trees: Seq[Tree]) extends GameClientMessage

  case object Died extends GameClientMessage

  case class NewKing(id: SledId) extends GameClientMessage

  case class RevengeTargets(sleds: Seq[SledId]) extends GameClientMessage

  case class KilledSled(sledId: SledId) extends GameClientMessage

  case class KilledBy(sledId: SledId) extends GameClientMessage

  case class Score(userName: String, score: Double) derives ReadWriter

  case class Scoreboard(myScore: Double, scores: Seq[Score]) extends GameClientMessage

  sealed trait AchievementBonus derives ReadWriter
  case object SpeedBonus  extends AchievementBonus
  case object HealthBonus extends AchievementBonus
  case object ScoreBonus  extends AchievementBonus

  case class AchievementMessage(
        bonus: AchievementBonus,
        title: String,
        description: String
  ) extends GameClientMessage

  case object Ping extends GameClientMessage
  case class RemoveItems(sharedItemType: SharedItemType, ids: Seq[Int])
      extends GameClientMessage

  case object ClientPong extends GameClientMessage

  case class GameTime(millis: Long, oneWayDelay: Int) extends GameClientMessage

  case class AddItems(items: Seq[SharedItem]) extends GameClientMessage

  sealed trait SharedItemType derives ReadWriter
  case object PowerUpItem  extends SharedItemType
  case object SledItem     extends SharedItemType
  case object SnowballItem extends SharedItemType
}
