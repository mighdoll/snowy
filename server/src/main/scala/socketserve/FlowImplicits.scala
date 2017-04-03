package socketserve

import scala.concurrent.{Future, Promise}
import scala.util.Success
import akka.stream.scaladsl.{Flow, Source}

object FlowImplicits {

  implicit class FlowOps[In, Out, Mat](flow: Flow[In, Out, Mat]) {

    /** run a side effecting function on messages in the flow */
    def foreach(fn: Out => Unit): Flow[In, Out, Mat] = {
      flow.map { m =>
        fn(m)
        m
      }
    }

    /** @return a future that completes with the materialized value of the flow */
    def peekMat: (Flow[In, Out, Mat], Future[Mat]) = {
      val promise = Promise[Mat]
      val newFlow = flow.mapMaterializedValue { mat =>
        promise.complete(Success(mat))
        mat
      }
      (newFlow, promise.future)
    }

  }

  implicit class SourceOps[Out, Mat](source: Source[Out, Mat]) {

    /** @return a future that completes with the materialized value of the source */
    def peekMat: (Source[Out, Mat], Future[Mat]) = {
      val promise = Promise[Mat]
      val newFlow = source.mapMaterializedValue { mat =>
        promise.complete(Success(mat))
        mat
      }
      (newFlow, promise.future)
    }

    /** run a side effecting function on messages in the flow */
    def foreach(fn: Out => Unit): Source[Out, Mat] = {
      source.map { m =>
        fn(m)
        m
      }
    }
  }
}
