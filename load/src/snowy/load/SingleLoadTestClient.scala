package snowy.load

import scribe.Logging
import snowy.robot.*
import snowy.util.ActorTypes.*

import java.util.concurrent.atomic.AtomicInteger

object SingleLoadTestClient {
  val nextUserId = new AtomicInteger()
}
import snowy.load.SingleLoadTestClient.nextUserId

class SingleLoadTestClient(using Actors[?], Measurement[?])(wsUrl: String)
    extends Logging {
  val userName  = s"loadTest-${nextUserId.getAndIncrement}"
  val robotHost = new LoadTestRobot(wsUrl)(api => new BlindRobotPlayer(api, userName))
//  val robotHost = new LoadTestRobot(wsUrl)(api => new RobotPlayer(api, userName))
}
