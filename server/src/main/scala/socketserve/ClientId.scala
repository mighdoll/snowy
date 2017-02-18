package socketserve

import java.util.concurrent.atomic.AtomicLong

object ClientId {
  val nextId = new AtomicLong
}

sealed trait ClientId {
  val id = ClientId.nextId.getAndIncrement()
}

class ConnectionId extends ClientId {
  override def toString = s"NetConnection_$id"
}

class RobotId extends ClientId {
  override def toString = s"InternalConnection_$id"
}
