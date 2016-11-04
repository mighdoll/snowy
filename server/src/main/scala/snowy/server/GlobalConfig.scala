package snowy.server

import java.io.File
import scala.collection.mutable.ListBuffer

/** singleton that holds the global .conf based config for the entire app */
object GlobalConfig {
  lazy val config = {
    val newConfig = ConfigUtil.configFromFilesAndResources(files)
    ConfigUtil.writeConfig(newConfig)
    newConfig
  }
  lazy val performanceReport = config.getBoolean("snowy.performanceReport")
  private val files          = ListBuffer[File]()

  def addConfigFiles(file: File*): Unit = {
    files.append(file: _*)
  }
}
