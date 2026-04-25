package snowy.server

import snowy.GameServerProtocol.PersistentControl
import socketserve.ClientId

import scala.collection.mutable

case class PendingControl(start: Millis, command: PersistentControl)

object PendingControl {
  def apply(command: PersistentControl, time: Millis = Millis.now()): PendingControl = {
    PendingControl(time, command)
  }
}

/** a collection of pending commands indexed by ConnectionId. At most one per
  * StartStopCommand is retained per ConnectionId.
  */
class PendingControls {

  private val commands =
    mutable.HashMap.empty[ClientId, mutable.Set[PendingControl]]

  /** record a pending command, replacing any previous matching command for this id. */
  def startCommand(id: ClientId, command: PersistentControl, time: Long): Unit = {
    removeCommand(id, command)
    commands.getOrElseUpdate(id, mutable.Set.empty) += PendingControl(command)
  }

  /** remove a pending command */
  def stopCommand(id: ClientId, command: PersistentControl, time: Long): Unit =
    removeCommand(id, command)

  def removeAll(id: ClientId): Unit = commands.remove(id)

  private def removeCommand(id: ClientId, command: PersistentControl): Unit = {
    commands.get(id).foreach { cmds =>
      cmds.filterInPlace(_.command != command)
    }
  }

  /** run a side effecting function on each pair */
  def foreachCommand(fn: (ClientId, PersistentControl, Millis) => Unit): Unit = {
    for {
      (id, set) <- commands
      value     <- set
    } {
      fn(id, value.command, value.start)
    }
  }

}

class Millis(val time: Long) extends AnyVal

object Millis {
  def now(): Millis = new Millis(System.currentTimeMillis())
}
