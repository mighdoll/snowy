package snowy.util

import akka.actor.ActorSystem
import akka.stream.{ActorAttributes, Attributes, Materializer, Supervision}
import scribe.Logger

/** Utility functions for working with akka */
object ActorUtil {

  /** return a Materializer for the given actor system */
  def materializerWithLogging(
        logger: Logger
  )(implicit system: ActorSystem): Materializer = Materializer(system)

  def loggingSupervision(logger: Logger): Attributes = {
    val decider: Supervision.Decider = { e =>
      logger.error("Unhandled exception in stream", e)
      Supervision.Stop
    }
    ActorAttributes.supervisionStrategy(decider)
  }
}
