package snowy.load

import boopickle.Default._
import snowy.playfield.Picklers._
import akka.util.ByteString
import snowy.GameClientProtocol.{ClientPong, Died, GameClientMessage, Ping}
import snowy.util.PartialMatch._

object FastUnpickle {
  private def pickleToByteString(m: GameClientMessage): ByteString =
    ByteString(Pickle(m).toByteBuffer)

  private val diedBytes       = pickleToByteString(Died)
  private val pingBytes       = pickleToByteString(Ping)
  private val clientPongBytes = pickleToByteString(ClientPong)

  /** Unpickle singleton client messages only (Died, Ping, ClientPong),
    * for messages return None.
    *
    * This reduces unpickling time dramatically, for load test
    * clients that don't need State, etc. */
  def partialUnpickleClientMessage(bytes: ByteString): Option[GameClientMessage] = {
    bytes pmatch {
      case `diedBytes`       => Died
      case `pingBytes`       => Ping
      case `clientPongBytes` => Ping
    }
  }

}


