package socketserve
import scala.concurrent.ExecutionContext
import akka.actor.ActorSystem
import snowy.util.MeasurementRecorder

/** some convenient syntax for passing common implicit parameters */
object ActorTypes {
  type Execution[_]   = ExecutionContext
  type Actors[_]      = ActorSystem
  type Measurement[_] = MeasurementRecorder
}
