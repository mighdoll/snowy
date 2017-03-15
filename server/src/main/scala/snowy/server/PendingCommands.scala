package snowy.server

import scala.collection.mutable
import snowy.GameServerProtocol.PersistentControl
import socketserve.ClientId

case class PendingCommand(start: Millis, command: PersistentControl)

object PendingCommand {
  def apply(command: PersistentControl, time: Millis = Millis.now()): PendingCommand = {
    PendingCommand(time, command)
  }
}

/** a collection of pending commands indexed by ConnectionId.
  * At most one per StartStopCommand is retained per ConnectionId. */
class PendingCommands {

  import scala.collection.mutable.{HashMap, MultiMap}

  val commands = new HashMap[ClientId, mutable.Set[PendingCommand]]
  with MultiMap[ClientId, PendingCommand]

  /** record a pending command, replacing any previous matching command for this id.  */
  def startCommand(id: ClientId, command: PersistentControl, time: Long): Unit = {
    removeCommand(id, command)
    commands.addBinding(id, PendingCommand(command))
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
