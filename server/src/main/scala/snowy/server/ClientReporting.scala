package snowy.server

import scala.collection.mutable
import com.typesafe.scalalogging.StrictLogging
import snowy.GameClientProtocol._
import snowy.playfield.PlayId.{BallId, PowerUpId, SledId}
import snowy.playfield.PowerUp
import snowy.server.ClientReporting.optNetId
import snowy.server.CommonPicklers.withPickledClientMessage
import snowy.server.GameTurn.TurnResults
import snowy.server.rewards.Achievements._
import snowy.util.ActorTypes.ParentSpan
import socketserve.{ClientId, ConnectionId, RobotId}
import snowy.measures.Span.time

/** Support for sending protocol messages about revised game state to the clients */
class ClientReporting(messageIO: MessageIO,
                      gameStateImplicits: GameStateImplicits,
                      connections: Traversable[ConnectionId],
                      robots: RobotHost)
    extends StrictLogging {
  import gameStateImplicits._
  import messageIO.{sendBinaryMessage, sendMessage}

  def sendToAllClients(message: GameClientMessage): Unit = {
    withPickledClientMessage(message) { pickledState =>
      connections.foreach { connectionId =>
        sendBinaryMessage(pickledState, connectionId)
      }
    }
  }

  def reportTurnResults[_: ParentSpan](turnResults: TurnResults): Unit =
    time("reportTurnResults") {
      import turnResults._
      reportSledIcings(icings)
      reportDeadSleds(deadSleds)
      reportAchievements(sledAchievements)
      reportDeadSnowballs(deadSnowBalls)
      reportUsedPowerUps(usedPowerUps)
      reportNewPowerUps(newPowerUps)
    }

  def joinedSled(connectionId: ClientId, sledId: SledId): Unit = {
    connectionId match {
      case robotId: RobotId    => robots.joined(robotId, sledId)
      case netId: ConnectionId => sendMessage(MySled(sledId), netId)
    }
  }

  private def reportSledIcings(sledKills: Traversable[SledIced]): Unit = {
    for {
      SledIced(serverSled, icedServerSled) <- sledKills
    } {
      reportKiller(serverSled.id, icedServerSled.id)
      reportDeadSled(serverSled.id, icedServerSled.id)
    }

    def reportKiller(killerSledId: SledId, deadSledId: SledId): Unit = {
      for {
        killerClientId     <- killerSledId.connectionId
        killerConnectionId <- optNetId(killerClientId)
      } {
        val killedSled = KilledSled(deadSledId)
        sendMessage(killedSled, killerConnectionId)
      }
    }

    def reportDeadSled(killerSledId: SledId, deadSledId: SledId): Unit = {
      for {
        deadClientId     <- deadSledId.connectionId
        deadConnectionId <- optNetId(deadClientId)
      } {
        val killedBy = KilledBy(killerSledId)
        sendMessage(killedBy, deadConnectionId)
      }
    }
  }

  /** Notify the client about notable achievements */
  private def reportAchievements(achievementsCollection: Traversable[Achievement]): Unit = {
    val reports =
      achievementsCollection.flatMap {
        case IcingStreak(sled, nth) =>
          Some(sled.id -> iceStreakMessage(nth))
        case RevengeIcing(sled, loserName) =>
          Some(sled.id -> revengeMessage(loserName))
        case SledOut(_) =>
          None  // currently reported elsewhere
        case SledIced(_, _) =>
          None // currently reported elsewhere
        case Kinged(sled, _) =>
          Some(sled.id -> kingMessage)
      }

    for {
      (sledId, report) <- reports
      clientId         <- sledId.connectionId
      connectionId     <- optNetId(clientId)
    } {
      sendMessage(report, connectionId)
    }
  }

  private def reportNewPowerUps(newPowerUps: Traversable[PowerUp]): Unit = {
    if (newPowerUps.nonEmpty) {
      val newItems = AddItems(newPowerUps.toSeq)
      sendToAllClients(newItems)
    }
  }

  private def reportUsedPowerUps(usedPowerUps: Traversable[PowerUpId]): Unit = {
    if (usedPowerUps.nonEmpty) {
      val removeItems = RemoveItems(PowerUpItem, usedPowerUps.toSeq)
      sendToAllClients(removeItems)
    }
  }

  private def reportDeadSnowballs(expiredBalls: Traversable[BallId]): Unit = {
    if (expiredBalls.nonEmpty) {
      val deaths = RemoveItems(SnowballItem, expiredBalls.toSeq)
      sendToAllClients(deaths)
    }
  }

  /** Notify clients about sleds that have been killed, remove sleds from the game */
  private def reportDeadSleds(dead: Traversable[SledOut]): Unit = {
    val deadSleds =
      dead.map {
        case SledOut(serverSled) => serverSled.id
      }.toSeq

    if (deadSleds.nonEmpty) {
      val deaths = RemoveItems(SledItem, deadSleds)
      sendToAllClients(deaths)

      for { sledId <- deadSleds; sled <- sledId.sled } {
        sendDied(sledId)
        if (logger.underlying.isInfoEnabled) {
          val connectIdStr =
            sledId.connectionId.map(id => s"(connection: $id) ").getOrElse("")
          logger.info(
            "reportDeadSleds: "
              + s"sled ${sledId.id}(${sledId.user.map(_.name)}) + "
              + s"killed $connectIdStr"
          )
        }
      }
    }
  }

  def sendDied(sledId: SledId): Unit = {
    sledId.connectionId match {
      case Some(netId: ConnectionId) => sendMessage(Died, netId)
      case Some(robotId: RobotId)    => robots.died(robotId)
      case None =>
        logger.warn(
          s"reapSled connection not found for sled: " +
            s"$sledId ${sledId.serverSled.map(_.user.name)}"
        )
    }
  }

  def reportGameTime(connectionId: ConnectionId, rtt: Long): Unit = {
    val msg = GameTime(System.currentTimeMillis(), (rtt / 2).toInt)
    messageIO.sendMessage(msg, connectionId)
  }

  /** Send the current score to the clients */
  def sendScores(users: mutable.Map[ClientId, User], gameTime: Long): Unit = {
    val topScores = {
      val rawScores = users.values.map { user =>
        Score(user.name, user.score)
      }.toSeq
      val sorted = rawScores.sortWith { (a, b) =>
        a.score > b.score
      }
      sorted.take(10)
    }

    users.collect {
      case (id: ConnectionId, user) if user.timeToSendScore(gameTime) =>
        user.scoreSent(gameTime)
        val scoreboard = Scoreboard(user.score, topScores)
        sendMessage(scoreboard, id)
    }
  }

  private val kingMessage: AchievementMessage = {
    AchievementMessage(
      SpeedBonus,
      s"King!",
      "You have the top score"
    )
  }
  private def revengeMessage(loserName: String): AchievementMessage = {
    AchievementMessage(
      SpeedBonus,
      s"Revenge on $loserName",
      "You iced someone who iced you"
    )
  }

  private def iceStreakMessage(nth: Int): AchievementMessage = {
    val amountString = nth match {
      case 2 => "Double Icing"
      case 3 => "Triple Icing"
      case n => n + " Icings"
    }

    AchievementMessage(
      SpeedBonus,
      amountString + " Icing",
      "Descriptions are boring"
    )
  }
}

object ClientReporting {
  def optNetId(id: ClientId): Option[ConnectionId] = {
    id match {
      case netId: ConnectionId => Some(netId)
      case _: RobotId          => None
    }
  }
}
