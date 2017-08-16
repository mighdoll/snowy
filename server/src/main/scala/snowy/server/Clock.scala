package snowy.server

trait Clock {
  def currentMillis:Long
}

object StandardClock extends Clock{
  def currentMillis = System.currentTimeMillis()
}

class ManualClock extends Clock {
  private var time = 0L

  def advanceTime(delta: Long): Unit = time += delta

  override def currentMillis: Long = time
}
