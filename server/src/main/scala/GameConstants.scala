object GameConstants {
  val maxSpeed = 1000  // max speed of sled in pixels per second
  val maxSkidTime = .8 // max seconds to complete a skid at full speed
  val gravity = -250.0 // pixels / second / second
  val maxFriction = 500.0 // pixels / second / second
  val minFriction = 50.0 // pixels / second / second
  val brakeSteepness = .8  // higher means braking effect peaks narrowly when skis are near 90 to travel
}
