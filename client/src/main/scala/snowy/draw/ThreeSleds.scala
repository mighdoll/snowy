package snowy.draw

import minithree.THREE
import minithree.THREE.{MeshPhongMaterialParameters, Object3D}
import minithree.raw.Vector3
import snowy.client.DrawState._
import snowy.client.{DrawState, UpdateGroup}
import snowy.playfield.PlayId.SledId
import snowy.playfield._

import scala.scalajs.js.Dynamic

object ThreeSleds {
  val sledGroup = new UpdateGroup[Sled](Groups.threeSleds)

  /** Revise position, etc. of a three js sled to match a playfield sled */
  def updateSled(playfieldSled: Sled, threeSled: Object3D, myPos: Vector3): Unit = {
    DrawState.setThreePosition(threeSled, playfieldSled, myPos)

    val body            = threeSled.children(1)
    val threeSledTurret = threeSled.children(0)
    val threeSledHealth = threeSled.children(2)

    body.rotation.y = playfieldSled.rotation
    body.scale.set(playfieldSled.radius, playfieldSled.radius, playfieldSled.radius)

    threeSledTurret.rotation.y = -playfieldSled.turretRotation
    threeSledTurret.position.set(
      math.sin(-playfieldSled.turretRotation) * playfieldSled.radius,
      0,
      math.cos(-playfieldSled.turretRotation) * playfieldSled.radius
    )

    threeSledHealth.scale.x = playfieldSled.health / playfieldSled.maxHealth
  }

  def updateThreeSleds(sleds: Set[Sled], mySled: Sled): Unit = {
    val myPos = Vector3(mySled.pos.x, 0, mySled.pos.y)

    // map of threeJs sleds, indexed by snowy sled id

    sleds.foreach { sled1 =>
      sledGroup.map.get(sled1.id) match {
        case Some(sled) => updateSled(sled1, sled, myPos)
        case None       => sledGroup.add(createSled(sled1, sled1.id == mySled.id, myPos))
      }
    }
  }

  // TODO comment
  def createSled(sled: Sled, friendly: Boolean, myPos: Vector3): Object3D = {
    val newSled = new THREE.Object3D()

    val skiMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = sled.skiColor.color.to0x(), shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )

    val bodyColor = sled.kind match {
      case BasicSled  => 0x00FFFF
      case TankSled   => 0xFF0000
      case GunnerSled => 0x0000FF
      case SpeedySled => 0x00FF00
      case SpikySled  => 0x888888
      case _          => 0xFFFFFF
    }
    val bodyMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = bodyColor, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val body = new THREE.Mesh(Geos.sled, bodyMat)
    val tur  = new THREE.Mesh(Geos.turret, Mats.turret)
    val health =
      new THREE.Mesh(Geos.health, if (friendly) Mats.healthColor else Mats.enemyHealth)

    val ski1 = new THREE.Mesh(Geos.ski, skiMat)
    val ski2 = new THREE.Mesh(Geos.ski, skiMat)

    val skiTip1 = new THREE.Mesh(Geos.skiTip, Mats.skiTip)
    val skiTip2 = new THREE.Mesh(Geos.skiTip, Mats.skiTip)

    health.position.z = -50

    skiTip1.position.z = 1.5 - 0.25 / 2
    skiTip2.position.z = 1.5 - 0.25 / 2

    ski1.add(skiTip1)
    ski2.add(skiTip2)

    ski1.position.set(-1.25, -2.5, 0)
    ski2.position.set(1.25, -2.5, 0)

    body.add(ski1)
    body.add(ski2)

    body.scale.set(sled.radius, sled.radius, sled.radius)

    newSled.add(tur)
    newSled.add(body)
    newSled.add(health)

    DrawState.setThreePosition(newSled, sled, myPos)

    newSled.name = sled.id.id.toString
    newSled
  }

  def removeSleds(deaths: Seq[SledId]): Unit = removeDeaths[Sled](sledGroup, deaths)
}
