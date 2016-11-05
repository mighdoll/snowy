package snowy.client

/** scala extractor objects to identify game keys in with pattern matching */
object Keys {

  class KeyMatch(keys: Int*) {
    def unapply(keyEvent: Int): Boolean =
      keys.contains(keyEvent)
  }

  object Up extends KeyMatch(87, 38)

  object Down extends KeyMatch(83, 40)

  object Right extends KeyMatch(68, 39)

  object Left extends KeyMatch(65, 37)

  object Space extends KeyMatch(32)
}
