package snowy.server

import java.io.File
import java.nio.charset.Charset
import java.nio.file.{Files, Paths}
import scala.collection.JavaConverters._
import com.typesafe.config.{Config, ConfigFactory}

object ConfigUtil {


  /** Load configuration with an extended set of .conf files.
    * In addition to the standard reference.conf/application.conf files,
    * a set of explicitly named filesystem .conf files and resource path .conf files
    * are loaded as well.
    * Explicitly named filesystem .conf files take priority in case of conflict,
    * with the first file named having the highest priority.
    * Second priority goes to explicitly named resources.
    * Third priority goes to standard typesafe.Config loaded files:
    * (the application.conf and reference.conf files.)
    */
  def configFromFilesAndResources(files: Traversable[File], resources: Traversable[String] = Nil)
  : Config = {
    val baseConfig = ConfigFactory.load()
    val fileConfigs = files.map { configFile =>
      ConfigFactory.parseFile(configFile)
    }.toSeq
    val resourceConfigs = resources.map(ConfigFactory.parseResources)
    val allConfigs: Seq[Config] = fileConfigs ++ resourceConfigs :+ baseConfig
    val combined = allConfigs.reduceLeft { (a, b) => a.withFallback(b) }
    combined.resolve()
  }

  /** write the composite .conf file */
  def writeConfig(config: Config) {
    val writeList = config.getStringList("snowy.dump-config").asScala
    writeList.headOption.foreach { fileName =>
      import java.nio.file.StandardOpenOption._
      val configString = config.root.render()
      val charSet = Charset.forName("UTF-8")
      val writer = Files.newBufferedWriter(Paths.get(fileName), charSet, TRUNCATE_EXISTING, CREATE)
      writer.write(configString)
      writer.close()
    }
  }

}
