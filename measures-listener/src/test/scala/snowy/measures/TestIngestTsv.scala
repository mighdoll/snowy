package snowy.measures

import java.nio.file.{Files, Path, Paths}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal
import com.tinkerpop.blueprints.impls.orient.OrientGraph
//import com.typesafe.scalalogging.StrictLogging
import scribe.Logging
import org.scalatest.PropSpec
import snowy.util.ResourceUtil
import snowy.util.FutureAwaiting._
import snowy.util.ActorUtil.materializerWithLogging

class TestIngestTsv extends PropSpec with Logging {
  // otherwise fails with: java.lang.NoClassDefFoundError: Could not initialize class com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal
  ODatabaseRecordThreadLocal.INSTANCE

  property("reading a tsv file of measurements") {
    implicit val system   = ActorSystem("Test")
    implicit val ignored2 = materializerWithLogging(logger)
    import system.dispatcher

    val path = ResourceUtil.filePath("test-measures.tsv")
    val measures =
      IngestTsvFile.ingestTsv(path)

    val result = measures.await()
    assert(result.spans === 21)
    assert(result.gauges === 2)
    assert(result.edges === 22)
    system.terminate()
  }
}
