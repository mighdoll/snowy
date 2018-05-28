//package minithree3
//
//import scala.scalajs.js
//import scala.scalajs.js.|
//
//@js.native
//@js.annotation.JSGlobal("PropertyBinding")
//class PropertyBinding protected () extends js.Object {
//  def this(rootNode: js.Any, path: String, parsedPath: js.Any = js.native) = this()
//  var path: String = js.native
//  var parsedPath: js.Any = js.native
//  var node: js.Any = js.native
//  var rootNode: js.Any = js.native
//  def getValue(targetArray: js.Any, offset: Double): js.Dynamic = js.native
//  def setValue(sourceArray: js.Any, offset: Double): Unit = js.native
//  def bind(): Unit = js.native
//  def unbind(): Unit = js.native
//  var BindingType: js.Dictionary[Double] = js.native
//  var Versioning: js.Dictionary[Double] = js.native
//  var GetterByBindingType: js.Array[js.Function] = js.native
//  var SetterByBindingTypeAndVersioning: js.Array[js.Array[js.Function]] = js.native
//}
//
//@js.native
//@js.annotation.JSGlobal("PropertyBinding")
//object PropertyBinding extends js.Object {
//  def create(root: js.Any, path: js.Any, parsedPath: js.Any = js.native): PropertyBinding | PropertyBinding.Composite = js.native
//  def parseTrackName(trackName: String): js.Dynamic = js.native
//  def findNode(root: js.Any, nodeName: String): js.Dynamic = js.native
//}