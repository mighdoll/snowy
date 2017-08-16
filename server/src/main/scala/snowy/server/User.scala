package snowy.server

import java.util.concurrent.ThreadLocalRandom
import scala.concurrent.duration._
import snowy.playfield.{SkiColor, SledType}
import snowy.util.FiniteQueue

object User {
  val scoreFrequency = 1.second.toMillis
}

import snowy.server.User._

/** A current game player.
  *
  * Note that the user might not currently have a sled in the game
  * if they have been killed and not yet rejoined.  */
class User(val name: String,
           val sledType: SledType,
           val skiColor: SkiColor,
           val createTime: Long) {

  /** points earned in the game */
  var score: Double               = 10
  private var nextScoreSend: Long = 0

  private val trackIcers = 4

  /** track of users that iced this user */
  val icedBy = new FiniteQueue[User](trackIcers)

  /** Scores are sent to users once per second (see scoreFrequency).
    * Users receive scores at jittered times, to spread the network load.
    * @return true if this user is overdue for a score update */
  def timeToSendScore(gameTime: Long): Boolean = {
    gameTime > nextScoreSend
  }

  /** record that the score was already sent*/
  def scoreSent(gameTime: Long): Unit = {
    val jitter = ThreadLocalRandom.current.nextDouble() * scoreFrequency
    nextScoreSend = gameTime + jitter.toInt
  }

  override def toString(): String = s"{$name} score:$score"
}
