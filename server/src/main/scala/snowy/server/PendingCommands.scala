package snowy.server

import scala.collection.mutable
import snowy.GameConstants.maxCommandDuration
import snowy.GameServerProtocol.StartStopCommand
import socketserve.ConnectionId

case class PendingCommand(start: Millis, command: StartStopCommand)

object PendingCommand {
  def apply(command: StartStopCommand, time: Millis = Millis.now()): PendingCommand = {
    PendingCommand(time, command)
  }
}

/** a collection of pending commands indexed by ConnectionId.
  * At most one per StartStopCommand is retained per ConnectionId. */
class PendingCommands {

  import scala.collection.mutable.{HashMap, MultiMap}

  val commands = new HashMap[ConnectionId, mutable.Set[PendingCommand]]
  with MultiMap[ConnectionId, PendingCommand]

  /** record a pending command, replacing any previous matching command for this id.  */
  def startCommand(id: ConnectionId, command: StartStopCommand, time: Long): Unit = {
    removeCommand(id, command)
    commands.addBinding(id, PendingCommand(command))
  }

  /** remove a pending command */
  def stopCommand(id: ConnectionId, command: StartStopCommand, time: Long): Unit =
    removeCommand(id, command)

  private def removeCommand(id: ConnectionId, command: StartStopCommand): Unit = {
    commands.get(id).map { cmds =>
      cmds.filter(_.command == command).map(cmds.remove)
    }
  }

//  /** remove all expired commands */
//  def removeExpired(gameTime: Millis): Unit = {
//    filterRemove { (_, command) =>
//      command.start.time + maxCommandDuration < gameTime.time
//    }
//  }

//  /** remove all pairs for which a function returns true */
//  def filterRemove(fn: (ConnectionId, PendingCommand) => Boolean): Unit = {
//    for {
//      (id, set) <- commands
//      value     <- set.toArray
//      if fn(id, value)
//    } {
//      set.remove(value)
//      if (set.isEmpty) commands.remove(id)
//    }
//  }

  /** run a side effecting function on each pair */
  def foreachCommand(fn: (ConnectionId, StartStopCommand, Millis) => Unit): Unit = {
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
