package snowy.server

import java.io.File

import scala.collection.mutable.ListBuffer

/** .conf-file configuration for the entire app */
object GlobalConfig {
  private var initialized = false
  lazy val config = {
    initialized = true
    val newConfig = ConfigUtil.configFromFilesAndResources(files)
    ConfigUtil.writeConfig(newConfig)
    newConfig
  }
  lazy val snowy    = config.getConfig("snowy")
  private val files = ListBuffer[File]()

  /** add .conf file that will be accessible to the program
    * Note: must be called at program start, before config is accessed
    */
  def addConfigFiles(file: File*): Unit = {
    assert(!initialized)
    files.append(file: _*)
  }
}
