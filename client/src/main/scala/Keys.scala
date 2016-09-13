/** scala extractor objects to identify game keys in with pattern matching */
object Keys {
  object Up {
    def unapply(keyEvent:String):Option[Unit] = {
      keyEvent match {
        case "ArrowUp" | "w" | "W" => Some(Unit)
        case _                     => None
      }
    }
  }
  object Down {
    def unapply(keyEvent:String):Option[Unit] = {
      keyEvent match {
        case "ArrowDown" | "s" | "S" => Some(Unit)
        case _                     => None
      }
    }
  }
  object Right {
    def unapply(keyEvent:String):Option[Unit] = {
      keyEvent match {
        case "ArrowRight" | "d" | "D" => Some(Unit)
        case _                     => None
      }
    }
  }
  object Left {
    def unapply(keyEvent:String):Option[Unit] = {
      keyEvent match {
        case "ArrowLeft" | "a" | "A" => Some(Unit)
        case _                     => None
      }
    }
  }
}


