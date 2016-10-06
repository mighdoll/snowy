object GameConstants {
  val playfield = Vec2d(1000, 2000)

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

  /** size of the tree trunk in pixels */
  val treeSize = Vec2d(10, 40)

  object Bullet {
    /** snowballs disappear after this many milliseconds */
    val lifetime = 10000

    /** radius in pixels */
    val size = 10

    /** acceleration due to recoil in pixels/sec/sec */
    val recoil = 30
  }

  object Points {
    /** points earned per pixel travelled */
    val travel = .0002

    /** % of the other users points earned from killing a sled */
    val sledKill = .5

    /** % of points lost if the sled is killed */
    val sledLoss = .5
  }


  object PushEnergy {
    /** speedup from the push button. in pixels / second / second */
    val force = 200.0

    /** time in seconds to recover from using up all push energy */
    val recoveryTime = 30.0

    /** max seconds of push energy available */
    val maxTime = 5
  }

  object Health {
    /** time in seconds to recover from almost 0 */
    val recoveryTime = 25.0
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

  object Collision {
    /** health cost from colliding into a tree */
    val treeCost = .25

    /** space in pixels to teleport away from tree after colliding */
    val treePadding = 2

    /** health cost from colliding into a tree at maximum speed */
    val maxTreeCost = .66

    /** minimum health after a tree collision */
    val treeMinHealth = .05

    /** health cost from being hit with a snowball */
    val snowballCost = .2

    /** at speeds less than this value (in pixels/sec), the sled takes no damage from hitting a tree.  */
    val safeSpeed = 50
  }
}
