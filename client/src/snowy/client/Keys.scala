package snowy.client

/** scala extractor objects to identify game keys in with pattern matching */
object Keys {

  class KeyMatch(keys: Int*) {
    def unapply(keyEvent: Int): Boolean =
      keys.contains(keyEvent)
  }

  object Up extends KeyMatch(87, 38, 73)

  object AutoFire extends KeyMatch(69, 191, 79) // E, /, o

  object Down extends KeyMatch(83, 40, 75)

  object Right extends KeyMatch(68, 39, 76)

  object Left extends KeyMatch(65, 37, 74)

  object TurretLeft extends KeyMatch(71)

  object TurretRight extends KeyMatch(72)

  object Shoot extends KeyMatch(32) // space
}
