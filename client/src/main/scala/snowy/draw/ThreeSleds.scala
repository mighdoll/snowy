package snowy.draw

import minithree.THREE
import minithree.THREE.{
  MeshBasicMaterialParameters,
  MeshPhongMaterialParameters,
  Object3D
}
import minithree.raw.Vector3
import org.scalajs.dom.{document, html, CanvasRenderingContext2D}
import snowy.client.DrawPlayfield._
import snowy.client.{DrawPlayfield, UpdateGroup}
import snowy.playfield.PlayId.SledId
import snowy.playfield._

import scala.scalajs.js.Dynamic

object ThreeSleds {
  val sledGroup = new UpdateGroup[Sled](Groups.threeSleds)

  /** Revise position, etc. of a three js sled to match a playfield sled */
  def updateSled(playfieldSled: Sled, threeSled: Object3D, myPos: Vector3): Unit = {
    DrawPlayfield.setThreePosition(threeSled, playfieldSled, myPos)

    val threeSledBody   = threeSled.children(1)
    val threeSledTurret = threeSled.children(0)
    val threeSledHealth = threeSled.children(2)

    val distanceBetween = playfieldSled.targetRotation - playfieldSled.rotation
    val tau             = math.Pi * 2
    val wrapping        = (distanceBetween % tau + (math.Pi * 3)) % tau - math.Pi
    threeSledBody.rotation.z = -math.sin(wrapping) * math.Pi / 6

    threeSledBody.rotation.y = playfieldSled.rotation
    threeSledBody.scale.set(
      playfieldSled.radius,
      playfieldSled.radius,
      playfieldSled.radius
    )

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
    val text = createText(sled.userName, 16)

    val ski1 = new THREE.Mesh(Geos.ski, skiMat)
    val ski2 = new THREE.Mesh(Geos.ski, skiMat)

    val skiTip1 = new THREE.Mesh(Geos.skiTip, Mats.skiTip)
    val skiTip2 = new THREE.Mesh(Geos.skiTip, Mats.skiTip)

    health.position.z = -50
    text.position.z = -80

    health.rotation.x = 1.5 * math.Pi

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

    newSled.add(text)

    DrawPlayfield.setThreePosition(newSled, sled, myPos)

    newSled.name = sled.id.id.toString
    newSled
  }

  def removeSleds(deaths: Seq[SledId]): Unit = removeDeaths[Sled](sledGroup, deaths)

  def nearestPow2(num: Double): Int =
    math.pow(2, math.round(math.log(num) / math.log(2))).toInt

  def getFixedCanvas(canvas: html.Canvas): html.Canvas = {
    val newCanvas = document.createElement("canvas").asInstanceOf[html.Canvas]
    newCanvas.width = nearestPow2(canvas.width)
    newCanvas.height = nearestPow2(canvas.height)
    val newCtx = newCanvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    newCtx.drawImage(canvas, 0, 0, newCanvas.width, newCanvas.height)
    newCanvas
  }

  // TODO Fix resizing
  def createText(text: String, scale: Double): Object3D = {
    val canvas = document.createElement("canvas").asInstanceOf[html.Canvas]
    val ctx    = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    val detail: Double = 4
    val height         = scale * detail
    ctx.font = height + "px Arial"
    val width = ctx.measureText(text).width

    canvas.width = width.toInt
    canvas.height = height.toInt

    ctx.clearRect(0, 0, width, height)
    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
    ctx.font = height + "px Arial"
    ctx.fillStyle = "black"
    ctx.fillText(text, width / 2, height / 2)

    val texture = new THREE.Texture(getFixedCanvas(canvas))
    texture.wrapS = THREE.RepeatWrapping
    texture.wrapT = THREE.RepeatWrapping

    val textPlane = new THREE.PlaneGeometry(width, height)
    val material = new THREE.MeshBasicMaterial(
      Dynamic
        .literal(map = texture, transparent = true, depthTest = false)
        .asInstanceOf[MeshBasicMaterialParameters]
    )
    val mesh = new THREE.Mesh(textPlane, material)

    mesh.rotation.x = 1.5 * math.Pi
    mesh.scale.set(1 / detail, 1 / detail, 1 / detail)
    texture.needsUpdate = true

    mesh
  }
}
