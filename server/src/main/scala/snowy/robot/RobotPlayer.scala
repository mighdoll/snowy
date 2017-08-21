package snowy.robot

import java.util.concurrent.ThreadLocalRandom

import snowy.AllLists
import snowy.GameServerProtocol._
import snowy.playfield._
import vector.Vec2d

import scala.util.Random

object RobotPlayer {
  private var id = 0

  def apply(api: RobotApi): RobotPlayer = {
    id = id + 1
    new RobotPlayer(api, AllLists.allNames(Random.nextInt(AllLists.allNames.size)))
  }
}

class RobotPlayer(api: RobotApi, name: String) extends Robot {
  val mySkis =
    AllLists.allSkis(ThreadLocalRandom.current.nextInt(AllLists.allSkis.length))
  val myType =
    AllLists.allSleds(ThreadLocalRandom.current.nextInt(AllLists.allSleds.length))

  api.sendToServer(Join(name, myType, mySkis))

  var mySledOpt: Option[Sled] = None

  def refresh(state: RobotGameState): Unit = {
    mySledOpt = state.mySled

    mySledOpt.foreach { mySled =>
      val robotGameTime = System.currentTimeMillis()
      val random        = ThreadLocalRandom.current.nextDouble()
      val commands = Seq(
        TargetAngle(aimAtNearest(mySled, state.otherSleds, state.snowballs))
      ) ++ (random match {
        case _ if random < .037 => Seq(Boost(robotGameTime))
        case _ if random < .070 => Seq(Start(Shooting, robotGameTime))
        case _ if random < .090 => Seq(Stop(Shooting, robotGameTime))
        case _                  => Seq()
      })

      commands.foreach { command =>
        api.sendToServer(command)
      }
    }
  }

  def aimAtNearest(mySled: Sled,
                   sleds: Traversable[Sled],
                   snowballs: Traversable[Snowball]): Double = {
      var closest = 1500.0
      var angle   = 0.0
      sleds.filterNot(sled => sled == mySled).foreach { sled =>
        val distance = sled.position - mySled.position
        if (distance.length <= closest) {
          closest = distance.length
          angle = distance.angle(Vec2d.unitUp)
        }
      }
      -angle
  }

  def killed(): Unit = {
    api.sendToServer(ReJoin)
  }
}
