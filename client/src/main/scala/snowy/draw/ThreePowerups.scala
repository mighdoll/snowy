package snowy.draw

import minithree.THREE
import minithree.THREE.{MeshLambertMaterialParameters, Object3D, Vector3}
import snowy.client.DrawPlayfield.removeDeaths
import snowy.client.{DrawPlayfield, UpdateGroup}
import snowy.playfield.PlayId.PowerUpId
import snowy.playfield.PowerUp

import scala.scalajs.js.Dynamic

class ThreePowerups(powerupGeo: THREE.Geometry) {
  val powerupMat = new THREE.MeshLambertMaterial(
    Dynamic
      .literal(color = 0x00ff00)
      .asInstanceOf[MeshLambertMaterialParameters]
  )

  val powerupGroup = new UpdateGroup[PowerUp](new THREE.Object3D())

  def wrapThreePowerups(myPos: Vector3): Unit = {
    powerupGroup.map.foreach {
      case (_, powerup) =>
        val newPos = DrawPlayfield.playfieldWrap(
          powerup.position,
          myPos,
          DrawPlayfield.playfieldSize
        )
        powerup.position.x = newPos.x
        powerup.position.z = newPos.z
    }
  }

  def addPowerup(powerup: PowerUp): Unit = {
    val newPowerup: Object3D = new THREE.Mesh(powerupGeo, powerupMat)
    newPowerup.scale.set(
      powerup.radius,
      powerup.radius,
      powerup.radius
    )

    newPowerup.position.set(powerup.position.x, 0, powerup.position.y)

    newPowerup.name = powerup.id.id.toString
    powerupGroup.add(newPowerup)
  }

  def removePowerup(deaths: Seq[PowerUpId]): Unit =
    removeDeaths[PowerUp](powerupGroup, deaths)
}
