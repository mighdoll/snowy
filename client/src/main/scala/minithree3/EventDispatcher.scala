package minithree3

import scala.scalajs.js
import org.scalajs.dom.raw._

@js.native
@js.annotation.JSGlobal("EventDispatcher")
class EventDispatcher extends js.Object {
  /**
     * Creates eventDispatcher object. It needs to be call with '.call' to add the functionality to an object.
     */
  /**
     * Adds a listener to an event type.
     * @param type The type of the listener that gets removed.
     * @param listener The listener function that gets removed.
     */
  def addEventListener(`type`: String, listener: js.Function1[Event, Unit]): Unit = js.native
  /**
     * Adds a listener to an event type.
     * @param type The type of the listener that gets removed.
     * @param listener The listener function that gets removed.
     */
  def hasEventListener(`type`: String, listener: js.Function1[Event, Unit]): Unit = js.native
  /**
     * Removes a listener from an event type.
     * @param type The type of the listener that gets removed.
     * @param listener The listener function that gets removed.
     */
  def removeEventListener(`type`: String, listener: js.Function1[Event, Unit]): Unit = js.native
  /**
     * Fire an event type.
     * @param type The type of event that gets fired.
     */
  def dispatchEvent(event: js.Any): Unit = js.native
}