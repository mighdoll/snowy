package snowy.draw

import minithree.THREE
import minithree.THREE.{MeshLambertMaterialParameters, Object3D}
import snowy.GameConstants

import scala.scalajs.js.Dynamic

object CreateGrid {
  def newGrid(): Object3D = {
    val xSpacing = 50
    val ySpacing = 50

    val xAmount = math.ceil(GameConstants.playfield.x / xSpacing).toInt
    val yAmount = math.ceil(GameConstants.playfield.y / ySpacing).toInt

    val material = new THREE.MeshLambertMaterial(
      Dynamic
        .literal(color = 0xe7f1fd, side = THREE.DoubleSide)
        .asInstanceOf[MeshLambertMaterialParameters]
    )

    val grid = new THREE.PlaneGeometry(
      GameConstants.playfield.x,
      GameConstants.playfield.y,
      xAmount,
      yAmount
    )
    for (i <- 0 until grid.vertices.length) {
      grid.vertices(i).z = -math.round(math.random) * 20
    }
    grid.computeFaceNormals()
    grid.computeVertexNormals()

    val mesh = new THREE.Mesh(grid, material)
    mesh.rotation.x = 1.5 * math.Pi
    mesh.position.x = GameConstants.playfield.x / 2
    mesh.position.z = GameConstants.playfield.y / 2
    mesh
  }
}
