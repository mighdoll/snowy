package snowy.client

import minithree.THREE

import scala.concurrent.{Future, Promise}
import scala.util.Success

class ThreeLoader() {
  val loader = new THREE.JSONLoader()
  def loadGeometries(urls: String*): Seq[Future[THREE.Geometry]] = {
    urls.map { url =>
      val promise = Promise[THREE.Geometry]
      loader.load(url, (geometry, _) => {
        promise.complete(Success(geometry))
      })
      promise.future
    }
  }
}
