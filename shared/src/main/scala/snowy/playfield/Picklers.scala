package snowy.playfield
import boopickle.CompositePickler
import boopickle.Default._
import snowy.GameClientProtocol.GameClientMessage
import snowy.GameServerProtocol.GameServerMessage
import snowy.playfield.PlayId.{BallId, PowerUpId, SledId, TreeId}
import vector.Vec2d

object Picklers {
  import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers._

  implicit val vec2dPickler: Pickler[Vec2d] =
    generatePickler[Vec2d]

  implicit val sledIdPickler    = playIdPickler[Sled]
  implicit val ballIdPickler    = playIdPickler[Snowball]
  implicit val TreeIdPickler    = playIdPickler[Tree]
  implicit val powerUpIdPickler = playIdPickler[PowerUp]
  implicit val anyPlayIdPickler = playIdPickler[Any]

  implicit val sledTypePickler: Pickler[SledType] =
    generatePickler[SledType]

  implicit val sledPickler: Pickler[Sled] =
    transformPickler((tupleToSled _).tupled)(sledToTuple)

  implicit val treePickler: Pickler[Tree] = {
    transformPickler((tupleToTree _).tupled)(treeToTuple)
  }
  implicit val snowballPickler: Pickler[Snowball] = {
    transformPickler((tupleToSnowball _).tupled)(snowballToTuple)
  }
  implicit val healthPowerUpPickler: Pickler[HealthPowerUp] = {
    transformPickler((tupleToHealthPowerUp _).tupled)(healthPowerUpToTuple)
  }
  implicit val speedPowerupPickler: Pickler[SpeedPowerUp] = {
    transformPickler((tupleToSpeedPowerUp _).tupled)(speedPowerUpToTuple)
  }

  implicit val gameClientMessage: Pickler[GameClientMessage] =
    generatePickler[GameClientMessage]

  implicit val gameServerMessage: Pickler[GameServerMessage] =
    generatePickler[GameServerMessage]

  implicit val shareSetPickler: CompositePickler[SharedItem] =
    compositePickler[SharedItem]
      .addConcreteType[HealthPowerUp]
      .addConcreteType[SpeedPowerUp]
      .addConcreteType[Snowball]
      .addConcreteType[Sled]

  def playIdPickler[A]: Pickler[PlayId[A]] =
    transformPickler[PlayId[A], Int] { (id: Int) =>
      new PlayId[A](id)
    } {
      _.id
    }

  private def treeToTuple(tree: Tree): (TreeId, Vec2d) =
    (tree.id, tree.position)

  private def tupleToTree(treeId: TreeId, newPosition: Vec2d): Tree = {
    new Tree() {
      override val id = treeId
      position = newPosition
    }
  }

  private def snowballToTuple(
        snowball: Snowball
  ): (BallId, SledId, Vec2d, Vec2d, Double, Double, Long, Double, Double, Double) = {
    (
      snowball.id,
      snowball.ownerId,
      snowball.position,
      snowball.speed,
      snowball.radius,
      snowball.mass,
      snowball.spawned,
      snowball.health,
      snowball.lifetime,
      snowball.impactDamage
    )
  }
  private def tupleToSnowball(newId: BallId,
                              ownerId: SledId,
                              newPosition: Vec2d,
                              speed: Vec2d,
                              radius: Double,
                              mass: Double,
                              spawned: Long,
                              health: Double,
                              lifetime: Double,
                              impactDamage: Double): Snowball = {
    new Snowball(
      ownerId = ownerId,
      speed = speed,
      radius = radius,
      mass = mass,
      spawned = spawned,
      health = health,
      lifetime = lifetime,
      impactDamage = impactDamage
    ) {
      override val id: BallId = newId

      position = newPosition
    }
  }

  private def tupleToSled(sledType: SledType,
                          newId: SledId,
                          userName: String,
                          newPosition: Vec2d,
                          newSpeed: Vec2d,
                          skiColor: SkiColor,
                          newRotation: Double,
                          newHealth: Double,
                          newTurretRotation: Double,
                          newLastShotTime: Long,
                          newLastBoostTime: Long,
                          maxSpeed: Int,
                          maxHealth: Double): Sled = {

    def setFields[A <: Sled](sled: A): A = {
      sled.rotation = newRotation
      sled.health = newHealth
      sled.turretRotation = newTurretRotation
      sled.lastShotTime = newLastShotTime
      sled.lastBoostTime = newLastBoostTime
      sled.speed = newSpeed
      sled.position = newPosition
      sled.maxSpeed = maxSpeed
      sled.maxHealth = maxHealth
      sled
    }

    def basicSled(): BasicSled = {
      val sled = new BasicSled(
        userName = userName,
        skiColor = skiColor
      ) {
        override val id: SledId = newId
      }
      setFields(sled)
    }

    def speedySled(): SpeedySled = {
      val sled = new SpeedySled(
        userName = userName,
        skiColor = skiColor
      ) {
        override val id: SledId = newId
      }
      setFields(sled)
    }

    def tankSled(): TankSled = {
      val sled = new TankSled(
        userName = userName,
        skiColor = skiColor
      ) {
        override val id: SledId = newId
      }
      setFields(sled)
    }

    sledType match {
      case BasicSledType  => basicSled()
      case SpeedySledType => speedySled()
      case TankSledType   => tankSled()
    }
  }

  private def sledToTuple(sled: Sled): (SledType,
                                        SledId,
                                        String,
                                        Vec2d,
                                        Vec2d,
                                        SkiColor,
                                        Double,
                                        Double,
                                        Double,
                                        Long,
                                        Long,
                                        Int,
                                        Double) = {
    val sledType: SledType = sled match {
      case _: BasicSled  => BasicSledType
      case _: SpeedySled => SpeedySledType
      case _: TankSled   => TankSledType
    }

    (
      sledType,
      sled.id,
      sled.userName,
      sled.position,
      sled.speed,
      sled.skiColor,
      sled.rotation,
      sled.health,
      sled.turretRotation,
      sled.lastShotTime,
      sled.lastBoostTime,
      sled.maxSpeed,
      sled.maxHealth
    )
  }

  private def healthPowerUpToTuple(powerUp: HealthPowerUp): (PowerUpId, Vec2d) = {
    (powerUp.id, powerUp.position)
  }

  private def tupleToHealthPowerUp(newId: PowerUpId, newPosition: Vec2d): HealthPowerUp = {
    new HealthPowerUp() {
      override val id: PowerUpId = newId
      position = newPosition
    }
  }

  private def speedPowerUpToTuple(powerUp: SpeedPowerUp): (PowerUpId, Vec2d) = {
    (powerUp.id, powerUp.position)
  }

  private def tupleToSpeedPowerUp(newId: PowerUpId, newPosition: Vec2d): SpeedPowerUp = {
    new SpeedPowerUp() {
      override val id: PowerUpId = newId
      position = newPosition
    }
  }
}
