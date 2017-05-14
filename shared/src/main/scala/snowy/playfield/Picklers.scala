package snowy.playfield
import boopickle.Default._
import snowy.playfield.PlayId.{BallId, SledId, TreeId}
import vector.Vec2d
import snowy.playfield.PlayfieldTracker.{
  nullSledTracker,
  nullSnowballTracker,
  nullTreeTracker
}

object Picklers {

  implicit val sledIdPickler = playIdPickler[Sled]
  implicit val ballIdPickler = playIdPickler[Snowball]
  implicit val TreeIdPickler = playIdPickler[Tree]

  implicit val sledPickler = {
    transformPickler((tupleToSled _).tupled)(sledToTuple _)
  }
  implicit val treePickler = {
    transformPickler((tupleToTree _).tupled)(treeToTuple _)
  }
  implicit val snowballPickler = {
    transformPickler((tupleToSnowball _).tupled)(snowballToTuple _)
  }
  implicit val nullSled     = nullSledTracker
  implicit val nullSnowball = nullSnowballTracker
  implicit val nullTree     = nullTreeTracker

  private def playIdPickler[A]: Pickler[PlayId[A]] =
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

  private def tupleToSled(newId: SledId,
                          userName: String,
                          newPosition: Vec2d,
                          speed: Vec2d,
                          kind: SledKind,
                          skiColor: SkiColor,
                          rotation: Double,
                          health: Double,
                          turretRotation: Double,
                          lastShotTime: Long,
                          lastBoostTime: Long): Sled = {
    new Sled(
      userName = userName,
      speed = speed,
      kind = kind,
      skiColor = skiColor,
      rotation = rotation,
      health = health,
      turretRotation = turretRotation,
      lastShotTime = lastShotTime,
      lastBoostTime = lastBoostTime
    ) {
      override val id: SledId = newId

      position = newPosition
    }
  }

  private def sledToTuple(sled: Sled): (SledId,
                                        String,
                                        Vec2d,
                                        Vec2d,
                                        SledKind,
                                        SkiColor,
                                        Double,
                                        Double,
                                        Double,
                                        Long,
                                        Long) = {
    (
      sled.id,
      sled.userName,
      sled.position,
      sled.speed,
      sled.kind,
      sled.skiColor,
      sled.rotation,
      sled.health,
      sled.turretRotation,
      sled.lastShotTime,
      sled.lastBoostTime
    )
  }

}
