package snowy.server

import java.util.concurrent.ThreadLocalRandom
import scala.concurrent.duration._


object User {
  val scoreFrequency = 1.second.toMillis
}

import User._

/** A current game player.
  *
  * Note that the user might not currently have a sled in the game
  * if they have been killed and not yet rejoined.  */
class User(val name: String, val robot: Boolean = false, val createTime: Long) {
  private var theScore: Double = 10
  private var nextScoreSend: Long = 0

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

  /** points earned in the game */
  def score: Double = theScore

  /** modify the score by adding a value */
  def addScore(value: Double): Unit = theScore = theScore + value

  /** modify the score by multiplying the current score */
  def multiplyScore(value: Double): Unit = theScore = theScore * value
}
