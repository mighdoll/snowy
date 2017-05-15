package snowy.client

import minithree.THREE._
import snowy.draw.{ThreePowerups, ThreeSleds, ThreeSnowballs}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class LoadedGeometries(threeSleds: ThreeSleds, threeSnowballs: ThreeSnowballs, threePowerups: ThreePowerups)

/** Load model geometry files from the server for WebGL rendering
  *
  * The geometries are loaded asynchronously and available via Future objects. */
class GeometryLoader {
  private val loader = new ThreeLoader()
  private val geometryFutures: Seq[Future[Geometry]] =
    loader.loadGeometries("sled/body.json", "sled/skis.json", "snowball/snowball.json", "powerup/powerup.json")
  private val geometriesLoaded: Future[Seq[Geometry]] = Future.sequence(geometryFutures)

  val threeGroupsFuture: Future[LoadedGeometries] = geometriesLoaded.map {
    case Seq(sled, skis, snowball, powerup) =>
      LoadedGeometries(new ThreeSleds(sled, skis), new ThreeSnowballs(snowball), new ThreePowerups(powerup))
  }
}
