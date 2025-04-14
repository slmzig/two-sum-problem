package com.megogo.assignment.config

import com.typesafe.config.ConfigFactory
import org.http4s.Uri
import pureconfig.{ConfigReader, ConfigSource}
import pureconfig.error.CannotConvert
import pureconfig.generic.auto._

final case class AppConfig(http: HttpConfig, targetResolver: TargetResolverConfig, cache: CacheConfig)

object AppConfig {

  implicit val uriReader: ConfigReader[Uri] =
    ConfigReader.fromString { str =>
      Uri.fromString(str).left.map(err => CannotConvert(str, "Uri", err.message))
    }

  def load(): AppConfig =
    ConfigSource.fromConfig(ConfigFactory.load()).loadOrThrow[AppConfig]

}
