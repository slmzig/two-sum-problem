package com.megogo.assignment.config

import scala.concurrent.duration.FiniteDuration

final case class HttpConfig(
    host: String,
    port: Int,
    requestTimeout: FiniteDuration,
    rateLimit: RateLimitConfig
)
