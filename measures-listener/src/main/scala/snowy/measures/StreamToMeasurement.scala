package snowy.measures

trait ReadMeasurement {
  val id: SpanId
  val name: String
  val start: EpochMicroseconds
  val parentId: Option[SpanId]
}

case class ReadSpan(
      override val id: SpanId,
      override val name: String,
      override val parentId: Option[SpanId],
      override val start: EpochMicroseconds,
      val duration: Long
) extends ReadMeasurement

case class ReadGaugeLong(
                     override val id: SpanId,
                     override val name: String,
                     override val parentId: Option[SpanId],
                     override val start: EpochMicroseconds,
                     val value: Long
                   ) extends ReadMeasurement

case class ReadGaugeDouble(
                          override val id: SpanId,
                          override val name: String,
                          override val parentId: Option[SpanId],
                          override val start: EpochMicroseconds,
                          val value: Double
                        ) extends ReadMeasurement

object StreamToMeasurement {
  def rowToMeasurement(row: String): Option[ReadMeasurement] = {
    val elems = row.split('\t')

    val name = elems(1)
    val id   = parseSpanId(elems(2))
    val parentId = elems(3) match {
      case "-" | "_" => None
      case pId       => Some(parseSpanId(pId))
    }
    val start    = EpochMicroseconds(elems(4).toLong)

    elems(0) match {
      case "S" =>
        val duration = elems(5).toLong
        val readSpan = ReadSpan(id, name, parentId, start, duration)
        Some(readSpan)
      case "L" =>
        val value = elems(5).toLong
        val readGauge = ReadGaugeLong(id, name, parentId, start, value)
        Some(readGauge)
      case "D" =>
        val value = elems(5).toDouble
        val readGauge = ReadGaugeDouble(id, name, parentId, start, value)
        Some(readGauge)
      case x =>
        println(s"no match on $x")
        None
    }
  }

  private def parseSpanId(id: String): SpanId = {
    val num = java.lang.Long.parseUnsignedLong(id, 16)
    SpanId(num)
  }
}
