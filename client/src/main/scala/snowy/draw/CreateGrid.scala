package snowy.draw

import minithree.THREE
import minithree.THREE.{Mesh, MeshLambertMaterialParameters}
import snowy.GameConstants

import scala.collection.mutable
import scala.scalajs.js.Dynamic

object CreateGrid {
  private val spacing     = 50
  private val gridColumns = math.ceil(GameConstants.playfield.x / spacing).toInt
  private val gridRows    = math.ceil(GameConstants.playfield.y / spacing).toInt

  // Plus one to include bottom and right borders
  private val gridColumns1 = gridColumns + 1
  private val gridRows1    = gridRows + 1

  private val heightMap: Seq[Double] = {
    val randomHeights = mutable.Seq(
      (for (_ <- 0 until gridColumns1 * gridRows1)
        yield -(math.round(math.random) * 20).toDouble): _*
    )

    // align heights at top and bottom so the seam isn't obvious when we wrap
    val lastRow = randomHeights.view(gridColumns1 * gridRows, randomHeights.size)
    for (i <- 0 until gridColumns1) {
      randomHeights(i) = lastRow(i)
    }

    // align heights on left and right so the seam isn't obvious when we wrap
    for (i <- randomHeights.indices by gridColumns1) {
      randomHeights(i) = randomHeights(i + gridColumns)
    }

    randomHeights
  }

  private val heightMap3: Seq[Double] = {
    def rowAtIndex(i: Int): Seq[Double] = {
      val row  = heightMap.slice(i, i + gridColumns)
      val row2 = heightMap.slice(i, i + gridColumns1)
      row ++ row ++ row2
    }

    val newMap: Seq[Double] = (for {
      i <- 0 until (gridColumns1 * gridRows) by gridColumns1
    } yield rowAtIndex(i)).flatten

    (newMap ++ newMap ++ newMap) ++ rowAtIndex(gridColumns1 * gridRows)
  }

  private val material = new THREE.MeshLambertMaterial(
    Dynamic
      .literal(color = 0xe7f1fd, side = THREE.DoubleSide)
      .asInstanceOf[MeshLambertMaterialParameters]
  )

  def newGrid(): Mesh = {
    val grid = new THREE.PlaneGeometry(
      GameConstants.playfield.x * 3,
      GameConstants.playfield.y * 3,
      gridColumns * 3,
      gridRows * 3
    )

    for (i <- grid.vertices.indices) {
      grid.vertices(i).z = heightMap3(i)
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
