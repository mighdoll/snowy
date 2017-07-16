package socketserve
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext
import snowy.measures.MeasurementRecorder

/** some convenient syntax for passing common implicit parameters */
object ActorTypes {
  type Execution[_]   = ExecutionContext
  type Actors[_]      = ActorSystem
  type Measurement[_] = MeasurementRecorder
}
