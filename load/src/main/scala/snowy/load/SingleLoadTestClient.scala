package snowy.load

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.ExecutionContext
import snowy.robot.StationaryRobot

object SingleLoadTestClient {
  val nextUserId = new AtomicInteger()
}
import SingleLoadTestClient.nextUserId

class SingleLoadTestClient(wsUrl:String)(implicit executionContext: ExecutionContext) {
  val userName = s"loadTest-${nextUserId.getAndIncrement}"
  val robotHost = new LoadTestRobot(wsUrl)(api =>
    new StationaryRobot(api, userName)
  )

}
