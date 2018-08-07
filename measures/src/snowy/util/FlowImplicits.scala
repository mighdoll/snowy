package snowy.util

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{Future, Promise}
import scala.util.Success
import akka.snowy.util.FixedBuffer
import akka.stream.scaladsl.{Flow, Source}
import snowy.measures.Nanoseconds

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

    /** add a buffering stage to the flow with OverflowStrategy.DropBuffer
      * semantics. Unlike the inbuilt buffer, provides a hook for logging if
      * the buffer overflows and is dropped.
      *
      * @param size number of elements in the buffer
      * @param fn called if the buffer dropped due to overflow
      * @return the flow with a buffering stage added
      */
    def fixedBuffer(size: Int, fn: => Unit): Flow[In, Out, Mat] = {
      val droppingFn = () => fn
      val buffer     = FixedBuffer[Out](size, droppingFn)
      flow.via(buffer)
    }

    /** a filtering stage buffers and optionally removes old elements
      * @param window messages older than this are considered for filtering
      * @param oldFn called on each old message to decide whether to filter it
      * @param overflowFn called (e.g. for logging) if the internal buffer overflows
      */
    def filterOld(window: FiniteDuration, // note untested
                  bufferSize: Int,
                  oldFn: (Out) => Boolean,
                  overflowFn: () => Unit): Flow[In, Out, Mat] = {

      case class Dated[T](item: T, time: Nanoseconds = Nanoseconds.current()) {
        def expired: Boolean = {
          val current = Nanoseconds.current().value
          current - time.value > window.toNanos
        }
      }

      flow
        .map(Dated(_))
        .fixedBuffer(bufferSize, overflowFn())
        .filter { dated =>
          val expired = dated.expired
          if (expired) {
            oldFn(dated.item)
          } else true
        }
        .map(_.item)
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
      source.via(Flow[Out].foreach(fn))
    }

    def fixedBuffer(size: Int, fn: => Unit): Source[Out, Mat] = {
      source.via(Flow[Out].fixedBuffer(size, fn))
    }
  }
}
