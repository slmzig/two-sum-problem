package com.megogo.assignment.http

import cats.effect.IO
import com.megogo.assignment.config.HttpConfig
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder

object Server {
  def serve(httpApp: HttpApp[IO], httpConfig: HttpConfig): IO[Unit] =
    BlazeServerBuilder[IO]
      .bindHttp(httpConfig.port, httpConfig.host)
      .withHttpApp(httpApp)
      .withResponseHeaderTimeout(httpConfig.requestTimeout)
      .serve
      .compile
      .drain
}
