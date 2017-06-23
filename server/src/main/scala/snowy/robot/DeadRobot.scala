package snowy.robot

import java.util.concurrent.ThreadLocalRandom

import snowy.AllLists
import snowy.GameServerProtocol._

import scala.util.Random

object DeadRobot {
  private var id = 0

  def apply(api: RobotApi): DeadRobot = {
    id = id + 1
    new DeadRobot(api, AllLists.allNames(Random.nextInt(AllLists.allNames.size)))
  }
}

class DeadRobot(api: RobotApi, name: String) extends Robot {
  val mySkis =
    AllLists.allSkis(ThreadLocalRandom.current.nextInt(AllLists.allSkis.length))
  val myType =
    AllLists.allSleds(ThreadLocalRandom.current.nextInt(AllLists.allSleds.length))

  api.sendToServer(Join(name, myType, mySkis))

  def refresh(state: RobotGameState): Unit =
    api.sendToServer(Start(Slowing, System.currentTimeMillis()))

  def killed(): Unit = api.sendToServer(ReJoin)
}
