package snowy.load

import java.util.concurrent.atomic.AtomicInteger
import com.typesafe.scalalogging.StrictLogging
import snowy.GameServerProtocol.{Join, ReJoin}
import snowy.robot._
import socketserve.ActorTypes._

object SingleLoadTestClient {
  val nextUserId = new AtomicInteger()
}
import SingleLoadTestClient.nextUserId

class SingleLoadTestClient[_: Actors: Measurement](wsUrl: String) extends StrictLogging {
  val userName = s"loadTest-${nextUserId.getAndIncrement}"
  val robotHost = new LoadTestRobot(wsUrl)(api => new BlindRobotPlayer(api, userName))
}
