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

/** a collection of pending commands indexed by ConnectionId.
  * At most one per StartStopCommand is retained per ConnectionId.
  */
class PendingControls {

  import scala.collection.mutable.{HashMap, MultiMap}

  val commands = new HashMap[ClientId, mutable.Set[PendingControl]]
    with MultiMap[ClientId, PendingControl]

  /** record a pending command, replacing any previous matching command for this id. */
  def startCommand(id: ClientId, command: PersistentControl, time: Long): Unit = {
    removeCommand(id, command)
    commands.addBinding(id, PendingControl(command))
  }

  /** remove a pending command */
  def stopCommand(id: ClientId, command: PersistentControl, time: Long): Unit =
    removeCommand(id, command)

  private def removeCommand(id: ClientId, command: PersistentControl): Unit = {
    commands.get(id).map { cmds =>
      cmds.filter(_.command == command).map(cmds.remove)
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
