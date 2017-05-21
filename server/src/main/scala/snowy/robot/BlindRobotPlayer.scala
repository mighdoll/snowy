package snowy.robot

import java.util.concurrent.ThreadLocalRandom
import com.typesafe.scalalogging.StrictLogging
import snowy.GameServerProtocol._
import socketserve.ActorTypes.Actors
import scala.concurrent.duration._
import akka.actor.ActorSystem

class BlindRobotPlayer[_: Actors](api: RobotApi, userName: String)
    extends Robot with StrictLogging {

  private val twoPi         = math.Pi * 2
  private var currentAngle  = 0d
  private val robotGameTime = System.currentTimeMillis()

  api.sendToServer(Join(userName))
  api.sendToServer(Start(Shooting, robotGameTime))
  updateAngle(randomDouble() * twoPi)

  val actorSystem = implicitly[ActorSystem]
  import actorSystem.dispatcher

  actorSystem.scheduler.schedule(100 milliseconds, 20 milliseconds) {
    randomTurn()
  }

  override def refresh(state: RobotGameState): Unit = {}

  override def killed(): Unit = {
    logger.info(s"$userName killed")
    api.sendToServer(ReJoin)
  }

  private def randomTurn(): Unit = {
    randomDouble() match {
      case d if d < .1 =>
        val delta = randomDouble() * .1
        updateAngle(currentAngle - delta)
      case d if d < .1 =>
        val delta = randomDouble() * .1
        updateAngle(currentAngle + delta)
      case _ =>
    }
  }

  private def roundAngle(angle: Double): Double = {
    val modAngle = angle % twoPi
    if (modAngle < 0)
      twoPi + modAngle
    else modAngle
  }

  private def updateAngle(angle: Double): Unit = {
    currentAngle = roundAngle(angle)
    api.sendToServer(TargetAngle(currentAngle))
  }

  private def randomDouble() = ThreadLocalRandom.current.nextDouble()
}
