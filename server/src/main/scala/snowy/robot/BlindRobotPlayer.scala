package snowy.robot

import java.util.concurrent.ThreadLocalRandom
import com.typesafe.scalalogging.StrictLogging
import snowy.GameServerProtocol._

class BlindRobotPlayer(api: RobotApi, userName: String)
    extends Robot with StrictLogging {

  val robotGameTime = System.currentTimeMillis()

  api.sendToServer(Join(userName))
  api.sendToServer(Start(Shooting, robotGameTime))
  api.sendToServer(TargetAngle(randomDouble() * math.Pi * 2))

  override def refresh(state: RobotGameState): Unit = {}

  override def killed(): Unit = {
    logger.info(s"$userName killed")
    api.sendToServer(ReJoin)
  }

  private def randomDouble() = ThreadLocalRandom.current.nextDouble()
}
