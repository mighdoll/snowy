/** scala extractor objects to identify game keys in with pattern matching */
object Keys {

  object Up {
    def unapply(keyEvent: String): Boolean = {
      keyEvent match {
        case "ArrowUp" | "w" | "W" => true
        case _                     => false
      }
    }
  }

  object Down {
    def unapply(keyEvent: String): Boolean = {
      keyEvent match {
        case "ArrowDown" | "s" | "S" => true
        case _                       => false
      }
    }
  }

  object Right {
    def unapply(keyEvent: String): Boolean = {
      keyEvent match {
        case "ArrowRight" | "d" | "D" => true
        case _                        => false
      }
    }
  }

  object Left {
    def unapply(keyEvent: String): Boolean = {
      keyEvent match {
        case "ArrowLeft" | "a" | "A" => true
        case _                       => false
      }
    }
  }

}


