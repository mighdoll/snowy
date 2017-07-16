package snowy.util

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import com.typesafe.scalalogging.Logger

/** Utility functions for working with akka */
object ActorUtil {

  /** return a actor materializer that logs errors on actor failure */
  def materializerWithLogging(
        logger: Logger
  )(implicit system: ActorSystem): ActorMaterializer = {
    val decider: Supervision.Decider = { e =>
      logger.error("Unhandled exception in actor", e)
      Supervision.Stop
    }

    val materializerSettings =
      ActorMaterializerSettings(system).withSupervisionStrategy(decider)
    ActorMaterializer(materializerSettings)(system)
  }
}
