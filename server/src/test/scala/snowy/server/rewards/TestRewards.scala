package snowy.server.rewards

import org.scalatest.PropSpec
import snowy.playfield.{BasicSledType, RedSkis, Sled}
import snowy.server.{ServerSled, User}
import snowy.server.rewards.Achievements.{IcingStreak, RevengeIcing}

class TestRewards extends PropSpec {
  private def dummyServerSled(): ServerSled = {
    val testUser = new User("testUser", BasicSledType, RedSkis, 0)
    ServerSled(Sled.dummy, testUser)
  }

  property("An Icing Streak of 5 Icings in a row is achieved twice. It rewards correctly") {
    val serverSled  = dummyServerSled()
    val achievement = IcingStreak(serverSled, 5)
    val rewards     = serverSled.rewards
    rewards.add(achievement)
    rewards.add(achievement)
    assert(serverSled.user.score === 130)    // 60 + 60 + initial 10
    assert(serverSled.sled.maxSpeed === 230) // initial 200 + 20 + 10
  }

  property("A revenge icing rewards correctly") {
    val serverSled  = dummyServerSled()
    val achievement = RevengeIcing(serverSled, "foofoo")
    val rewards     = serverSled.rewards
    rewards.add(achievement)
    assert(serverSled.user.score === 110)     // initial 10 + 100
    assert(serverSled.sled.maxHealth === 2.5) // initial 1 + 1.5
  }

}
