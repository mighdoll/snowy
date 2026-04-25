package snowy.measures

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal
import org.scalatest.propspec.AnyPropSpec
import scribe.Logging
import snowy.util.ResourceUtil
import snowy.util.FutureAwaiting.*

class TestIngestTsv extends AnyPropSpec with Logging {
  // otherwise fails with: java.lang.NoClassDefFoundError: Could not initialize class com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal
  ODatabaseRecordThreadLocal.instance()

  property("reading a tsv file of measurements") {
    implicit val system: ActorSystem        = ActorSystem("Test")
    implicit val materializer: Materializer = Materializer(system)
    import system.dispatcher

    val path     = ResourceUtil.filePath("test-measures.tsv")
    val measures = IngestTsvFile.ingestTsv(path)

    val result = measures.await()
    assert(result.spans === 21)
    assert(result.gauges === 2)
    assert(result.edges === 22)
    system.terminate()
  }
}
