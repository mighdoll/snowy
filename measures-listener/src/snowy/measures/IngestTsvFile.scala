package snowy.measures

import java.nio.file.Path
import scala.collection.mutable
import scala.concurrent.Future
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Framing, Keep, Sink, Source}
import akka.util.ByteString
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.orient._
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import snowy.measures.StreamToMeasurement.rowToMeasurement
import snowy.util.ActorTypes._
import snowy.util.FlowImplicits._
import scala.collection.JavaConverters._

object IngestTsvFile extends Logging {
  // otherwise fails with: java.lang.NoClassDefFoundError: Could not initialize class com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal

  case class IngestResults(spans: Int, gauges: Int, edges: Int)

  def ingestTsv[_: Execution: Materializer](path: Path): Future[IngestResults] = {
    val graphFactory = new OrientGraphFactory("plocal:/Users/lee/spans-db")
    val db           = graphFactory.getTx
    val graphDb      = db.getRawGraph
    graphDb.declareIntent(new OIntentMassiveInsert())

    val measureStream = readTsv(path)
    val span          = Span.root("storeMeasures")(NullMeasurementRecorder)
    storeMeasures(db, measureStream).andThen { case _ =>
      val timeSpan       = span.finishNow()
      val elapsedSeconds = timeSpan.value / (1000.0 * 1000)
      println(s"elapsed time: $elapsedSeconds seconds")
      db.shutdown()
    }
  }

  def storeMeasures[_: Execution: Materializer](
        db: OrientBaseGraph,
        source: Source[ReadMeasurement, Future[IOResult]]
  ): Future[IngestResults] = {

    var spanCount    = 0
    var edgeCount    = 0
    var gaugeCount   = 0
    val commitPeriod = 20000
    case class Edge(vertex: Vertex, id: SpanId, parent: Option[SpanId])

    def storeVertices(): Future[Seq[Edge]] = {
      logger.warn("loading vertices")
      val links =
        source
          .collect {
            case span: ReadSpan =>
              spanCount += 1
              val fields = Map[String, Object](
                "name"      -> span.name,
                "duration"  -> span.duration.asInstanceOf[Object],
                "start"     -> span.start.value.asInstanceOf[Object],
                "measureId" -> span.id.value.asInstanceOf[Object]
              ).asJava
              val vertex = db.addVertex("class:Span", fields)
              Edge(vertex, span.id, span.parentId)
            case gauge: ReadGaugeLong =>
              gaugeCount += 1
              val fields = Map[String, Object](
                "name"      -> gauge.name,
                "value"     -> gauge.value.asInstanceOf[Object],
                "start"     -> gauge.start.value.asInstanceOf[Object],
                "measureId" -> gauge.id.value.asInstanceOf[Object]
              ).asJava
              val vertex = db.addVertex("class:GaugeLong", fields)
              Edge(vertex, gauge.id, gauge.parentId)
            case gauge: ReadGaugeDouble =>
              gaugeCount += 1
              val fields = Map[String, Object](
                "name"      -> gauge.name,
                "value"     -> gauge.value.asInstanceOf[Object],
                "start"     -> gauge.start.value.asInstanceOf[Object],
                "measureId" -> gauge.id.value.asInstanceOf[Object]
              ).asJava
              val vertex = db.addVertex("class:GaugeDouble", fields)
              Edge(vertex, gauge.id, gauge.parentId)
          }
          .foreach { _ =>
            if (spanCount % commitPeriod == 0) {
              db.commit()
              logger.warn(s"read spanCount: $spanCount")
            }
          }

      links.toMat(Sink.seq)(Keep.right).run().map { edges =>
        db.commit()
        edges
      }
    }

    def storeEdges(edges: Seq[Edge]): Unit = {
      val vertexMap = {
        val map = mutable.HashMap[SpanId, Vertex]()
        edges.foreach { edge =>
          map(edge.id) = edge.vertex
        }
        map
      }
      for {
        edge     <- edges
        parentId <- edge.parent
        parent   <- vertexMap.get(parentId)
      } {
        edgeCount = edgeCount + 1
        db.addEdge("class:Parent", edge.vertex, parent, null)
        if (edgeCount % commitPeriod == 0) {
          db.commit()
          logger.warn(s"read edgeCount: $edgeCount")
        }
      }
    }

    val futureDone =
      storeVertices().map { edges =>
        storeEdges(edges)
      }

    futureDone
      .map { _ =>
        IngestResults(spanCount, gaugeCount, edgeCount)
      }

  }

  private def setMeasurementFields(measurement: ReadMeasurement, vertex: Vertex): Unit = {

    vertex.setProperty("start", measurement.start.value)
    vertex.setProperty("measureId", measurement.id.value)
  }

  def readTsv(path: Path): Source[ReadMeasurement, Future[IOResult]] = {
    val lineStream =
      FileIO
        .fromPath(path)
        .via(
          Framing
            .delimiter(
              ByteString("\n"),
              maximumFrameLength = 4096,
              allowTruncation = true
            )
        )
        .map(_.utf8String)

    val measurementStream =
      lineStream
        .drop(1)
        .map(rowToMeasurement)
        .collect { case Some(measurement) => measurement }

    measurementStream
  }

}
