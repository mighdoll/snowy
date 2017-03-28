package snowy.robot

import java.util.concurrent.ThreadLocalRandom

import snowy.GameConstants

import scala.util.Random
import snowy.GameServerProtocol._
import snowy.playfield._
import vector.Vec2d

object StationaryRobot {
  private var id = 0

  def apply(api: RobotApi): StationaryRobot = {
    id = id + 1
    new StationaryRobot(api, RobotNames.allNames(Random.nextInt(RobotNames.allNames.size)))
  }
}

class StationaryRobot(api: RobotApi, name: String) extends Robot {
  val mySkis =
    SkiColors.allSkis(ThreadLocalRandom.current.nextInt(SkiColors.allSkis.length))
  val myType =
    SledKinds.allSleds(ThreadLocalRandom.current.nextInt(SledKinds.allSleds.length))

  api.sendToServer(Join(name, myType, mySkis))

  var mySledOpt: Option[Sled] = None

  def refresh(state: RobotGameState): Unit = {
    mySledOpt = state.mySled

    mySledOpt.foreach { mySled =>
      val robotGameTime = System.currentTimeMillis()
      val random        = ThreadLocalRandom.current.nextDouble()
      val commands = Seq(TargetAngle(aimAtNearest(mySled, state.otherSleds, state.snowballs))) ++ (random match {
        case _ if random < .037 => Seq(Boost(robotGameTime))
        case _ if random < .070 => Seq(Start(Shooting, robotGameTime))
        case _ if random < .090 => Seq(Stop(Shooting, robotGameTime))
        case _ => Seq()
      })

      commands.foreach { command =>
        api.sendToServer(command)
      }
    }
  }

  def aimAtNearest(mySled: Sled,
                   sleds: Traversable[Sled],
                   snowballs: Traversable[Snowball]): Double = {

    var closestBall = mySled.bulletLaunchPosition.length
    var ballAngle   = 0.0
    val closeSnowballs = snowballs
      .filterNot(ball => ball.ownerId == mySled.id)
      .filter(
        ball => (mySled._position - ball._position).length < closestBall
      )
    if (closeSnowballs.nonEmpty) {
      snowballs.foreach { ball =>
        val distance = ball._position - mySled._position
        if (distance.length <= closestBall) {
          closestBall = distance.length
          ballAngle = distance.angle(Vec2d.unitUp)
        }
      }
      -ballAngle
    } else {
      var closest = 1500.0
      var angle   = 0.0
      sleds.filterNot(sled => sled == mySled).foreach { sled =>
        val distance = sled._position - mySled._position
        if (distance.length <= closest) {
          closest = distance.length
          angle = distance.angle(Vec2d.unitUp)
        }
      }
      -angle
    }
  }

  def killed(): Unit = {
    api.sendToServer(ReJoin)
  }
}
