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
  private val files          = ListBuffer[File]()

  /** add .conf file that will be accessible to the program */
  def addConfigFiles(file: File*): Unit = {
    files.append(file: _*)
  }
}
