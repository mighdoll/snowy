package snowy.load

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.ExecutionContext
import snowy.robot.RobotPlayer
import socketserve.ActorTypes._

object SingleLoadTestClient {
  val nextUserId = new AtomicInteger()
}
import SingleLoadTestClient.nextUserId

class SingleLoadTestClient[_: Actors](wsUrl: String) {
  val userName  = s"loadTest-${nextUserId.getAndIncrement}"
  val robotHost = new LoadTestRobot(wsUrl)(api => new RobotPlayer(api, userName))
}
