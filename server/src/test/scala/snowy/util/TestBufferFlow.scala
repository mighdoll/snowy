package akka.snowy

import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.socketserve.FixedBuffer
import akka.stream.impl.Buffer
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.{Attributes, DelayOverflowStrategy}
import com.typesafe.scalalogging.StrictLogging
import org.scalatest.PropSpec
import snowy.util.FutureAwaiting._
import socketserve.ActorUtil.materializerWithLogging
import socketserve.FlowImplicits._

class TestBufferFlow extends PropSpec with StrictLogging {

  ignore("FixedBuffer allows peeking at its size") {
    implicit val system       = ActorSystem()
    implicit val materializer = materializerWithLogging(logger)

    val dropping = () => println(s"dropping buffer")

    val buffer = FixedBuffer[Int](3, dropping)
    val (flow, done) =
      Source
        .fromIterator(() => Iterator.from(1))
        .via(buffer)
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
