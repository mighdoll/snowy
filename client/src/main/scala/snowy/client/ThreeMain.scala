package snowy.client

import minithree.THREE
import minithree.THREE.WebGLRendererParameters
import org.scalajs.dom.raw.Event
import org.scalajs.dom.{document, window}

import scala.scalajs.js.Dynamic

object ThreeMain {
  var width  = window.innerWidth
  var height = window.innerHeight

  val renderer = new THREE.WebGLRenderer(
    Dynamic
      .literal(antialias = false, canvas = document.getElementById("game-c"))
      .asInstanceOf[WebGLRendererParameters]
  )
  renderer.setClearColor(new THREE.Color(0xe7f1fd), 1)
  renderer.setPixelRatio(
    if (!window.devicePixelRatio.isNaN) window.devicePixelRatio else 1
  )

  val scene  = new THREE.Scene()
  var camera = new THREE.PerspectiveCamera(45, width / height, 1, 5000)
  camera.position.set(0, 100, -100)
  camera.lookAt(new THREE.Vector3(0, 0, 0))

  window.addEventListener("resize", { _: Event =>
    resize()
  })
  resize()

  def resize(): Unit = {
    width = window.innerWidth
    height = window.innerHeight
    camera.aspect = Math.min(width / height, 3)
    camera.updateProjectionMatrix()
    renderer.setSize(width, height)
    renderer.render(scene, camera)
  }
}