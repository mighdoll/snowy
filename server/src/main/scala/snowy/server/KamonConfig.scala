package snowy.server

import kamon.ConfigProvider

class KamonConfig extends ConfigProvider {
  def config = GlobalConfig.config
}
