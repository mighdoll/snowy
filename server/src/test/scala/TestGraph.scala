import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.{ActorMaterializer, ClosedShape, Graph, OverflowStrategy}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source}
import org.scalatest._

class TestGraph extends PropSpec {
  def withActorSystem(fn: ActorSystem => Unit): Unit = {
    val system = ActorSystem()
    try {
      fn(system)
    } finally {
      system.terminate()
    }

  }
  property("graph flow basics ") {
    withActorSystem { implicit system =>
      implicit val materializer = ActorMaterializer()

      val graph = GraphDSL.create() {
        implicit builder =>
          import GraphDSL.Implicits._
          val source = Source(1L to 10L)
          val ignore = Sink.ignore
          val first = Flow[Long].take(5).map { time =>
//            println(s"got first: $time")
          }
          val ongoing = Flow[Long].drop(5).map { time =>
//            println(s"got next: $time")
          }
          val bcast = builder.add(Broadcast[Long](2))
          source ~> bcast ~> first ~> ignore
          bcast ~> ongoing ~> ignore
          ClosedShape
      }

      val g = RunnableGraph.fromGraph(graph)

      g.run()
    }
  }
}
