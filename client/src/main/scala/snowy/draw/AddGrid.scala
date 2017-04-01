package snowy.draw

import minithree.THREE
import minithree.THREE.{LineBasicMaterialParameters, Object3D}
import snowy.GameConstants

import scala.scalajs.js
import scala.scalajs.js.Dynamic
import scala.scalajs.js.typedarray.Float32Array

object AddGrid {
  def createGrid(): Object3D = {
    val xSpacing = 50
    val ySpacing = 50

    val xAmount = math.ceil(GameConstants.playfield.x / xSpacing).toInt
    val yAmount = math.ceil(GameConstants.playfield.y / ySpacing).toInt

    val material = new THREE.LineBasicMaterial(
      Dynamic
        .literal(color = 0x0)
        .asInstanceOf[LineBasicMaterialParameters]
    )

    val grid = new THREE.Object3D()
    for (i <- 0 to xAmount) {
      val geo = new THREE.BufferGeometry()
      val vertices = new Float32Array(
        js.Array(
          i * xSpacing,
          0,
          yAmount * ySpacing,
          i * xSpacing,
          0,
          0
        )
      )
      geo.addAttribute("position", new THREE.BufferAttribute(vertices, 3))
      val line = new THREE.Line(geo, material)
      grid.add(line)
    }

    for (j <- 0 to yAmount) {
      val geo = new THREE.BufferGeometry()
      val vertices = new Float32Array(
        js.Array(
          0,
          0,
          j * ySpacing,
          xAmount * xSpacing,
          0,
          j * ySpacing
        )
      )
      geo.addAttribute("position", new THREE.BufferAttribute(vertices, 3))
      val line = new THREE.Line(geo, material)
      grid.add(line)
    }
    grid
  }
}
