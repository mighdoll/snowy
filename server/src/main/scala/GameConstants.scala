object GameConstants {
  /** max speed of sled in pixels per second */
  val maxSpeed = 1000

  /** max seconds to complete a skid at full speed */
  val maxSkidTime = .8

  /** pixels / second / second */
  val gravity = -250.0

  /** stop pending commands after this many milliseconds */
  val commandDuration = 2000

  /** point up on the screen, towards smaller Y values */
  val downhillRotation = math.Pi

  /** Time to turn 180 degrees in seconds */
  val turnTime = .75

  object PushTime {
    /** speedup from the push button. in pixels / second / second */
    val force = 200

    /** time to recover push */
    val recoveryTime = 30

    /** max seconds of push force available */
    val maxTime = 5
  }

  object Friction {
    /** friction from the slowdown button. in pixels / second / second */
    val slowButtonFriction = 400

    /** friction from ski angled away from direction of travel in pixels / second / second */
    val maxFriction = 500.0

    /** friction from ski angled along direction of travel in pixels / second / second */
    val minFriction = 50.0

    /** higher means ski friction is mostly when skis are perpendicular to current direction */
    val brakeSteepness = .8
  }

}
