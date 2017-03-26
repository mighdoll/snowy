package snowy.draw

import minithree.THREE
import minithree.THREE.Object3D
import minithree.raw.Vector3
import snowy.client.DrawPlayfield.{removeDeaths, Geos, Groups, Mats}
import snowy.client.{DrawPlayfield, UpdateGroup}
import snowy.playfield.PlayId.BallId
import snowy.playfield.Snowball

object ThreeSnowballs {
  val snowballGroup = new UpdateGroup[Snowball](Groups.threeSnowballs)
  def updateThreeSnowballs(snowballs: Set[Snowball], myPos: Vector3): Unit = {
    snowballs.foreach { snowball1 =>
      snowballGroup.map.get(snowball1.id) match {
        case Some(snowball) => DrawPlayfield.setThreePosition(snowball, snowball1, myPos)
        case None           => snowballGroup.add(createSnowball(snowball1, myPos))
      }
    }
  }

  def createSnowball(snowball: Snowball, myPos: Vector3): Object3D = {
    val newSnowball: Object3D = new THREE.Mesh(Geos.snowball, Mats.snowball)
    newSnowball.scale.set(
      snowball.radius,
      snowball.radius,
      snowball.radius
    )

    DrawPlayfield.setThreePosition(newSnowball, snowball, myPos)

    newSnowball.name = snowball.id.id.toString
    newSnowball
  }

  def removeSnowballs(deaths: Seq[BallId]): Unit =
    removeDeaths[Snowball](snowballGroup, deaths)
}
