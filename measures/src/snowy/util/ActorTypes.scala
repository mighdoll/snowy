package snowy.util

import scala.concurrent.ExecutionContext
import akka.actor.ActorSystem
import snowy.measures.{MeasurementRecorder, Span}

/** some convenient syntax for passing common implicit parameters */
object ActorTypes {
  type Execution[_]    = ExecutionContext
  type Actors[_]       = ActorSystem
  type Measurement[_]  = MeasurementRecorder
  type Materializer[_] = akka.stream.Materializer
  type ParentSpan[_]   = Span
}
