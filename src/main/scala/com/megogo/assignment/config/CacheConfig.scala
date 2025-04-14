package com.megogo.assignment.config

import scala.concurrent.duration.FiniteDuration

final case class CacheConfig(ttl: FiniteDuration)