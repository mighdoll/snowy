package minithree3

import scala.scalajs.js

@js.native
@js.annotation.JSGlobal("Clock")
class Clock protected () extends js.Object {
  def this(autoStart: Boolean = js.native) = this()
  /**
     * @param autoStart Automatically start the clock.
     */
  /**
     * If set, starts the clock automatically when the first update is called.
     */
  var autoStart: Boolean = js.native
  /**
     * When the clock is running, It holds the starttime of the clock.
     * This counted from the number of milliseconds elapsed since 1 January 1970 00:00:00 UTC.
     */
  var startTime: Double = js.native
  /**
     * When the clock is running, It holds the previous time from a update.
     * This counted from the number of milliseconds elapsed since 1 January 1970 00:00:00 UTC.
     */
  var oldTime: Double = js.native
  /**
     * When the clock is running, It holds the time elapsed between the start of the clock to the previous update.
     * This parameter is in seconds of three decimal places.
     */
  var elapsedTime: Double = js.native
  /**
     * This property keeps track whether the clock is running or not.
     */
  var running: Boolean = js.native
  /**
     * Starts clock.
     */
  def start(): Unit = js.native
  /**
     * Stops clock.
     */
  def stop(): Unit = js.native
  /**
     * Get the seconds passed since the clock started.
     */
  def getElapsedTime(): Double = js.native
  /**
     * Get the seconds passed since the last call to this method.
     */
  def getDelta(): Double = js.native
}