package snowy.client

import minithree.THREE._
import snowy.draw.ThreeSleds

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/** Load model geometry files from the server for WebGL rendering
  *
  * The geometries are loaded asynchronously and available via Future objects. */
class LoadedGeometries {
  private val loader = new ThreeLoader()
  private val Seq(sledFuture, skisFuture) =
    loader.loadGeometries("sled/body.json", "sled/skis.json")
  private val geometriesLoaded: Future[Seq[Geometry]] =
    Future.sequence(Seq(sledFuture, skisFuture))

  val threeSledsFuture: Future[ThreeSleds] = geometriesLoaded.map {
    case Seq(sled, skis) => new ThreeSleds(sled, skis)
  }
}
