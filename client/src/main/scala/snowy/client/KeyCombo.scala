package snowy.client
import scala.collection.mutable
import org.scalajs.dom._

/** Enable listening for pairs of keys simultaneously pressed,
  * e.g. for pressing debug key and a second key */
class KeyCombos {
  private val listeners = mutable.Map[Char, Char => Unit]()
  private val active    = mutable.Map[Char, Char => Unit]()

  window.addEventListener("keydown", keyDown)
  window.addEventListener("keyup", keyUp)

  /** register a function to be called when a base key and a second key
    * are held down simultaneously */
  def listen(baseKey: Char)(fn: Char => Unit): Unit = {
    listeners(baseKey) = fn
  }

  /** unregister a key combo listener key */
  def unlisten(baseKey: Char): Unit = {
    listeners.remove(baseKey)
    active.remove(baseKey)
  }

  private def keyDown(e: KeyboardEvent): Unit = {
    for { key <- shiftedKey(e) } {
      println(s"key down: ${e.keyCode}")

      for {
        (activeKey, fn) <- active
        if key != activeKey // ignore key repeats
      } {
        println(s"combo key pressed: '$activeKey' '$key' ${e.charCode} ${e.keyCode}")
        fn(key) // notify two keys held down
      }

      listeners.get(key).foreach { fn =>
        active(key) = fn // start watching for next key in a combo
      }
    }
  }

  private def keyUp(e: KeyboardEvent): Unit = {
    shiftedKey(e).foreach { _ =>
      val rawKey = e.keyCode.toChar
      active.remove(rawKey.toUpper)
      active.remove(rawKey.toLower)
    }
  }

  /** Optionally return the shifted or unshifted char for a keyboard event
    * return None if the key isn't a normal character */
  private def shiftedKey(e: KeyboardEvent): Option[Char] = {
    if (e.keyCode < ' ' || e.keyCode > '~') {
      None
    } else {
      val rawKey = e.keyCode.toChar
      if (e.getModifierState("Shift"))
        Some(rawKey.toUpper)
      else
        Some(rawKey.toLower)
    }
  }

}
