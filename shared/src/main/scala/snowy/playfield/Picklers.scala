package snowy.playfield
import boopickle.{CompositePickler, ConstPickler}
import boopickle.Default._
import snowy.playfield.PlayId.{BallId, PowerUpId, SledId, TreeId}
import vector.Vec2d

object Picklers {
  import snowy.playfield.PlayfieldTracker.ImplicitNullTrackers._

  implicit val sledIdPickler    = playIdPickler[Sled]
  implicit val ballIdPickler    = playIdPickler[Snowball]
  implicit val TreeIdPickler    = playIdPickler[Tree]
  implicit val powerUpIdPickler = playIdPickler[PowerUp]
  implicit val anyPlayIdPickler = playIdPickler[Any]

  implicit val basicSledTypePickler: Pickler[BasicSledType.type] =
    ConstPickler(BasicSledType)

  implicit val tankSledTypePickler: Pickler[TankSledType.type] =
    ConstPickler(TankSledType)

  implicit val gunnerSledTypePickler: Pickler[GunnerSledType.type] =
    ConstPickler(GunnerSledType)

  implicit val speedySledTypePickler: Pickler[SpeedySledType.type] =
    ConstPickler(SpeedySledType)

  implicit val spikySledTypePickler: Pickler[SpikySledType.type] =
    ConstPickler(SpikySledType)

  implicit val prototypeSledTypePickler: Pickler[PrototypeSledType.type] =
    ConstPickler(PrototypeSledType)

  implicit val sledTypePickler: Pickler[SledType] =
    compositePickler[SledType]
      .addConcreteType[BasicSledType.type]
      .addConcreteType[TankSledType.type]
      .addConcreteType[GunnerSledType.type]
      .addConcreteType[SpeedySledType.type]
      .addConcreteType[SpikySledType.type]
      .addConcreteType[PrototypeSledType.type]

  implicit val sledPickler: Pickler[Sled] =
    transformPickler((tupleToSled _).tupled)(sledToTuple)

  implicit val treePickler: Pickler[Tree] = {
    transformPickler((tupleToTree _).tupled)(treeToTuple)
  }
  implicit val snowballPickler: Pickler[Snowball] = {
    transformPickler((tupleToSnowball _).tupled)(snowballToTuple)
  }
  implicit val healhPowerUpPickler: Pickler[HealthPowerUp] = {
    transformPickler((tupleToHealthPowerUp _).tupled)(healthPowerUpToTuple)
  }

  implicit val shareSetPickler: CompositePickler[SharedItem] =
    compositePickler[SharedItem]
      .addConcreteType[HealthPowerUp]
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
                          newLevel: Int): Sled = {

    def setFields[A <: Sled](sled: A): A = {
      sled.rotation = newRotation
      sled.health = newHealth
      sled.turretRotation = newTurretRotation
      sled.lastShotTime = newLastShotTime
      sled.lastBoostTime = newLastBoostTime
      sled.speed = newSpeed
      sled.position = newPosition
      sled.level = newLevel
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

    def tankSled(): TankSled = {
      val sled = new TankSled(
        userName = userName,
        skiColor = skiColor
      ) {
        override val id: SledId = newId
      }
      setFields(sled)
    }

    def gunnerSled(): GunnerSled = {
      val sled = new GunnerSled(
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

    def spikySled(): SpikySled = {
      val sled = new SpikySled(
        userName = userName,
        skiColor = skiColor
      ) {
        override val id: SledId = newId
      }
      setFields(sled)
    }

    def prototypeSled(): PrototypeSled = {
      val sled = new PrototypeSled(
        userName = userName,
        skiColor = skiColor
      ) {
        override val id: SledId = newId
      }
      setFields(sled)
    }

    sledType match {
      case BasicSledType     => basicSled()
      case TankSledType      => tankSled()
      case GunnerSledType    => gunnerSled()
      case SpeedySledType    => speedySled()
      case SpikySledType     => spikySled()
      case PrototypeSledType => prototypeSled()
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
                                        Int) = {
    val sledType: SledType = sled match {
      case _: BasicSled     => BasicSledType
      case _: TankSled      => TankSledType
      case _: GunnerSled    => GunnerSledType
      case _: SpeedySled    => SpeedySledType
      case _: SpikySled     => SpikySledType
      case _: PrototypeSled => PrototypeSledType
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
      sled.level
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

}
