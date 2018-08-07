package minithree2.THREE

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

// TODO what should be vars?
@js.native
@JSGlobal("THREE.Box2")
class Box2(var min: js.UndefOr[Vector2], var max: js.UndefOr[Vector2]) extends js.Object {}

@js.native
@JSGlobal("THREE.Box3")
class Box3(var min: js.UndefOr[Vector3], var max: js.UndefOr[Vector3]) extends js.Object {}

@js.native
@JSGlobal("THREE.Color")
class Color extends js.Object {
  var r: js.UndefOr[Double] = js.native
  var g: js.UndefOr[Double] = js.native
  var b: js.UndefOr[Double] = js.native

  def this(r: js.UndefOr[Double] = js.native,
           g: js.UndefOr[Double] = js.native,
           b: js.UndefOr[Double] = js.native) = this()
  def this(hex: Int) = this()
  def this(string: String) = this()
}

@js.native
@JSGlobal("THREE.Cylindrical")
class Cylindrical(radius: Float, theta: Float, y: Float) extends js.Object {}

@js.native
@JSGlobal("THREE.Euler")
class Euler(x: js.UndefOr[Float],
            y: js.UndefOr[Float],
            z: js.UndefOr[Float],
            order: js.UndefOr[String])
    extends js.Object {}

@js.native
@JSGlobal("THREE.Frustum")
class Frustum(p0: js.UndefOr[Plane],
              p1: js.UndefOr[Plane],
              p2: js.UndefOr[Plane],
              p3: js.UndefOr[Plane],
              p4: js.UndefOr[Plane],
              p5: js.UndefOr[Plane])
    extends js.Object {}

// TODO what is this?
@js.native
@JSGlobal("THREE.Interpolant")
class Interpolant() extends js.Object {}

@js.native
@JSGlobal("THREE.Line3")
class Line3(start: Vector3, end: Vector3) extends js.Object {}

@js.native
@JSGlobal("THREE.Math")
object Math extends js.Object {}

@js.native
@JSGlobal("THREE.Matrix3")
class Matrix3 extends js.Object {}

@js.native
@JSGlobal("THREE.Matrix4")
class Matrix4 extends js.Object {}

@js.native
@JSGlobal("THREE.Plane")
class Plane(normal: js.UndefOr[Vector3], constant: js.UndefOr[Float]) extends js.Object {}

@js.native
@JSGlobal("THREE.Quaternion")
class Quaternion(x: Float, y: Float, z: Float, w: Float) extends js.Object {}

@js.native
@JSGlobal("THREE.Ray")
class Ray(origin: js.UndefOr[Vector3], direction: Vector3) extends js.Object {}

@js.native
@JSGlobal("THREE.Sphere")
class Sphere(origin: Vector3, radius: Float) extends js.Object {}

@js.native
@JSGlobal("THREE.Spherical")
class Spherical(radius: Float, phi: Float, theta: Float) extends js.Object {}

@js.native
@JSGlobal("THREE.Triangle")
class Triangle(a: Vector3, b: Vector3, c: Vector3) extends js.Object {}

@js.native
@JSGlobal("THREE.Vector2")
class Vector2(x: Float, y: Float) extends js.Object {}

@js.native
@JSGlobal("THREE.Vector3")
class Vector3(x: Double, y: Double, z: Double) extends js.Object {}

@js.native
@JSGlobal("THREE.Vector4")
class Vector4(x: Float, y: Float, z: Float, w: Float) extends js.Object {}
