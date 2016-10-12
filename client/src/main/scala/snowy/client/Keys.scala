package snowy.client

/** scala extractor objects to identify game keys in with pattern matching */
object Keys {

  class KeyMatch(keys: String*) {
    def unapply(keyEvent: String): Boolean =
      keys.contains(keyEvent)
  }

  object Up extends KeyMatch("ArrowUp", "w", "W")

  object Down extends KeyMatch("ArrowDown", "s", "S")

  object Right extends KeyMatch("ArrowRight", "d", "D")

  object Left extends KeyMatch("ArrowLeft", "a", "A")

}


