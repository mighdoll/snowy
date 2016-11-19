package snowy.robot

import snowy.playfield.{BasicSkis, StationaryTestSled}

object StationaryRobot {
  private var id = 0

  def apply(api: RobotApi): StationaryRobot = {
    id = id + 1
    new StationaryRobot(api, s"Stay$id")
  }
}

class StationaryRobot(api: RobotApi, name: String) extends Robot {
  var mySled = api.join(name, StationaryTestSled, BasicSkis)

  def refresh(state: RobotState): Unit = {
//    val randomAngle = math.Pi * 2 / ThreadLocalRandom.current.nextDouble()
//    api.sendToServer(TurretAngle(randomAngle))
  }

  def killed(): Unit = {
    mySled = api.rejoin()
  }
}
