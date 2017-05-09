package snowy.draw

import minithree.THREE
import minithree.THREE.{MeshLambertMaterialParameters, Object3D, Vector3}
import snowy.client.DrawPlayfield.removeDeaths
import snowy.client.{DrawPlayfield, UpdateGroup}
import snowy.playfield.PlayId.BallId
import snowy.playfield.{BasicSled, Snowball}

import scala.scalajs.js.Dynamic

object ThreeSnowballs {
  val snowballGeo = new THREE.BoxGeometry(2, 2, 2)
  val snowballMat = new THREE.MeshLambertMaterial(
    Dynamic
      .literal(color = 0x1878f0)
      .asInstanceOf[MeshLambertMaterialParameters]
  )

  val snowballGroup = new UpdateGroup[Snowball](new THREE.Object3D())
  def updateThreeSnowballs(snowballs: Set[Snowball], myPos: Vector3): Unit = {
    snowballs.foreach { snowball1 =>
      snowballGroup.map.get(snowball1.id) match {
        case Some(snowball) => DrawPlayfield.setThreePosition(snowball, snowball1, myPos)
        case None           => snowballGroup.add(createSnowball(snowball1, myPos))
      }
    }
  }

  def createSnowball(snowball: Snowball, myPos: Vector3): Object3D = {
    val newSnowball: Object3D = new THREE.Mesh(snowballGeo, snowballMat)
    newSnowball.scale.set(
      snowball.radius,
      snowball.radius,
      snowball.radius
    )

    DrawPlayfield.setThreePosition(newSnowball, snowball, myPos)

    // TODO: Use actual sled height instead
    newSnowball.position.y = BasicSled.radius * 2
    newSnowball.name = snowball.id.id.toString
    newSnowball
  }

  def removeSnowballs(deaths: Seq[BallId]): Unit =
    removeDeaths[Snowball](snowballGroup, deaths)
}
