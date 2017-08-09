package snowy.server

import snowy.playfield.Sled
import snowy.server.rewards.Rewards

case class ServerSled(sled: Sled, user: User) {
  val rewards = new Rewards
  def id = sled.id
}
