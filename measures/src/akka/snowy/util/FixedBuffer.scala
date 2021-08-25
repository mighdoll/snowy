package akka.snowy.util

import akka.stream.Attributes
import akka.stream.impl.fusing.GraphStages.SimpleLinearGraphStage
import akka.stream.impl.{Buffer => BufferImpl}
import akka.stream.stage.{GraphStageLogic, InHandler, OutHandler}
import akka.snowy.util.DoNothing.nullFn

/** a custom buffering stage that drops the buffer on overflow.
  * @param droppingFn is called if the buffer is dropped
  */
final case class FixedBuffer[T](size: Int, droppingFn: () => Unit = nullFn)
    extends SimpleLinearGraphStage[T] {

  val f                     = Function
  val buffer: BufferImpl[T] = akka.stream.impl.Buffer[T](size, size)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with InHandler with OutHandler {

      def enqueueAction(elem: T): Unit = {
        if (buffer.isFull) {
          droppingFn()
          buffer.clear()
        }
        buffer.enqueue(elem)
        pull(in)
      }

      override def preStart(): Unit = {
        pull(in)
      }

      override def onPush(): Unit = {
        val elem = grab(in)
        // If out is available, then it has been pulled but no dequeued element has been delivered.
        // It means the buffer at this moment is definitely empty,
        // so we just push the current element to out, then pull.
        if (isAvailable(out)) {
          push(out, elem)
          pull(in)
        } else {
          enqueueAction(elem)
        }
      }

      override def onPull(): Unit = {
        if (buffer.nonEmpty) push(out, buffer.dequeue())
        if (isClosed(in)) {
          if (buffer.isEmpty) completeStage()
        } else if (!hasBeenPulled(in)) {
          pull(in)
        }
      }

      override def onUpstreamFinish(): Unit = {
        if (buffer.isEmpty) completeStage()
      }

      setHandlers(in, out, this)
    }
}

object DoNothing {
  val nullFn = () => {}
}
