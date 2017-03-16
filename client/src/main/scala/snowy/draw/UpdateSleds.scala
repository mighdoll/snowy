package snowy.draw

import minithree.THREE
import minithree.THREE.{MeshPhongMaterialParameters, Vector3}
import snowy.GameConstants
import snowy.client.DrawState2
import snowy.client.DrawState2._
import snowy.playfield.PlayId.SledId
import snowy.playfield._

import scala.scalajs.js.Dynamic

object UpdateSleds {
  def updateCsleds(sleds: Set[Sled], mySled: Sled): Unit = {
    val myPos = new Vector3(mySled.pos.x, 0, mySled.pos.y)
    sleds.foreach { sled1 =>
      var idExists = false
      Groups.csleds.children.zipWithIndex.foreach {
        case (aSled, index) =>
          if (aSled.name == sled1.id.id.toString) {
            idExists = true
            val csled = Groups.csleds.children(index)

            csled
              .children(1)
              .setRotationFromAxisAngle(
                new THREE.Vector3(0, 1, 0),
                sled1.rotation
              )
            csled.children(1).scale.x = sled1.radius
            csled.children(1).scale.y = sled1.radius
            csled.children(1).scale.z = sled1.radius

            val newPos = transformPositionMod(
              new Vector3(sled1.pos.x, 0, sled1.pos.y),
              myPos,
              new Vector3(GameConstants.playfield.x, 0, GameConstants.playfield.y)
            )

            csled.position.x = newPos.x
            csled.position.z = newPos.z

            csled.children(0).rotation.y = -sled1.turretRotation

            csled
              .children(0)
              .position
              .set(
                math.sin(-sled1.turretRotation) * sled1.radius,
                0,
                math.cos(-sled1.turretRotation) * sled1.radius
              )

            csled.children(2).scale.x = sled1.health / sled1.maxHealth
          }
      }

      if (!idExists) {
        addSled(sled1, sled1.id == mySled.id, myPos)
      }
    }
  }

  def addSled(sled: Sled, friendly: Boolean, myPos: Vector3): Unit = {
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

    newSled.add(tur)
    newSled.add(body)
    newSled.add(health)

    val newPos = DrawState2.transformPositionMod(
      new Vector3(sled.pos.x, 0, sled.pos.y),
      myPos,
      new Vector3(GameConstants.playfield.x, 0, GameConstants.playfield.y)
    )

    newSled.position.x = newPos.x
    newSled.position.z = newPos.z
    newSled.name = sled.id.id.toString
    Groups.csleds.add(newSled)
  }

  def removeSleds(deaths: Seq[SledId]): Unit = {
    val ids = deaths.map(_.id.toString)
    Groups.csleds.children = Groups.csleds.children.filter { sled =>
      !ids.contains(sled.name)
    }
  }
}
