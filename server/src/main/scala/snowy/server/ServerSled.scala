package snowy.server

import snowy.playfield.{IcingRecords, Sled}
import snowy.server.rewards.Rewards

case class ServerSled(sled: Sled, user: User, gameTime:()=>Long) {
  val rewards = new Rewards
  def id      = sled.id

  val icingRecords = new IcingRecords()

}
