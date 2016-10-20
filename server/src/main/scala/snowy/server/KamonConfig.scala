package snowy.server

import com.typesafe.config.Config
import kamon.ConfigProvider

object KamonConfig {
  private var _config: Option[Config] = None

  def config: Config = _config.get

  def setConfig(config: Config): Unit = _config = Some(config)
}

class KamonConfig extends ConfigProvider {
  def config = KamonConfig.config
}
