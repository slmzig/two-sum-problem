package com.megogo.assignment.config

import com.typesafe.config.ConfigFactory
import pureconfig.ConfigSource
import pureconfig.generic.auto._

final case class AppConfig(http: HttpConfig, defaultTarget: Int, maxRequestsPerMinute: Int)

object AppConfig {
  def load(): AppConfig =
    ConfigSource.fromConfig(ConfigFactory.load()).loadOrThrow[AppConfig]

}
