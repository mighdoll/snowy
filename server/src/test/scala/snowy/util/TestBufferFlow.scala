package akka.snowy

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import scala.concurrent.duration._
import akka.stream.OverflowStrategies._
import akka.stream.{
  Attributes,
  BufferOverflowException,
  DelayOverflowStrategy,
  OverflowStrategy
}
import akka.stream.impl.fusing.GraphStages.SimpleLinearGraphStage
import akka.stream.stage.{GraphStageLogic, InHandler, OutHandler}
import com.typesafe.scalalogging.StrictLogging
import org.scalatest.PropSpec
import socketserve.ActorUtil.materializerWithLogging
import socketserve.FlowImplicits._
import snowy.util.FutureAwaiting._

class TestBufferFlow extends PropSpec with StrictLogging {

  property("PeekBuffer allows peeking at its size") {
    implicit val system       = ActorSystem()
    implicit val materializer = materializerWithLogging(logger)

    val buffer = PeekBuffer[Int](3)
    val (flow, done) =
      Source
        .fromIterator(() => Iterator.from(1))
        .via(buffer)
          .foreach {x =>
            val used = buffer.buffer.used
            val capacity = buffer.buffer.capacity
            println(s"buffer $used/$capacity")
          }
        .delay(100.millis, DelayOverflowStrategy.backpressure)
        .withAttributes(Attributes.inputBuffer(2, 2))
        .take(10)
        .watchTermination()(Keep.right)
        .peekMat

    flow
      .to(Sink.foreach { x =>
        println(s"$x")
      })
      .run

    done.await().await()
  }
}

import akka.stream.impl.{Buffer => BufferImpl}

final case class PeekBuffer[T](size: Int) extends SimpleLinearGraphStage[T] {

  val buffer: BufferImpl[T] = akka.stream.impl.Buffer[T](size, size)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with InHandler with OutHandler {

      val enqueueAction: T ⇒ Unit = { elem ⇒
        if (buffer.isFull) buffer.clear()
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
