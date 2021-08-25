package snowy.playfield

/** Record of when this sled has knocked out other sleds in the current game session.
  * Used to generate Rewards like IcingStreak
  */
class IcingRecords {
  var total: Int     = 0
  var streak: Int    = 0
  var lastTime: Long = 0
}
