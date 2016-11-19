package snowy.robot

import java.util.concurrent.ThreadLocalRandom

import snowy.GameServerProtocol._
import snowy.playfield.{BasicSkis, BasicSled, Sled}
import vector.Vec2d

object StationaryRobot {
  private var id = 0

  def apply(api: RobotApi): StationaryRobot = {
    id = id + 1
    new StationaryRobot(api, s"Stay$id")
  }
}

class StationaryRobot(api: RobotApi, name: String) extends Robot {
  var mySled = api.join(name, BasicSled, BasicSkis)

  def refresh(state: RobotState): Unit = {
    val gameTime = System.currentTimeMillis()
    val random   = ThreadLocalRandom.current.nextDouble()
    val commands = Seq(TurretAngle(aimAtNearest(state.sleds))) ++ (random match {
      case _ if random < .005 => Seq(Start(Left, gameTime))
      case _ if random < .010 => Seq(Start(Right, gameTime))
      case _ if random < .030 => Seq(Stop(Right, gameTime), Stop(Left, gameTime))
      case _ if random < .040 => Seq(Start(Pushing, gameTime))
      case _ if random < .060 => Seq(Stop(Pushing, gameTime))
      case _ if random < .070 => Seq(Start(Shooting, gameTime))
      case _ if random < .090 => Seq(Stop(Shooting, gameTime))
      case _                  => Seq()
    })
    commands.foreach { command =>
      api.sendToServer(command)
    }
  }

  def aimAtNearest(sleds: Traversable[Sled]): Double = {
    var closest = 1000.0
    var angle   = 0.0
    sleds.filterNot(elm => elm == mySled).foreach { sled =>
      val distance = sled._position - mySled._position
      if (distance.length <= closest) {
        closest = distance.length
        angle = distance.angle(Vec2d.unitUp)
      }
    }
    -angle
  }

  def killed(): Unit = {
    mySled = api.rejoin()
  }
}
