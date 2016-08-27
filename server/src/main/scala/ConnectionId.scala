import java.util.concurrent.atomic.AtomicLong

object ConnectionId {
  val nextId = new AtomicLong
}

class ConnectionId {
  val id = ConnectionId.nextId.getAndIncrement()
  override def toString = s"Connection_$id"
}
