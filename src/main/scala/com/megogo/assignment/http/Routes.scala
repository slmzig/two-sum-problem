package com.megogo.assignment.http

import cats.effect._
import com.megogo.assignment.config.HttpConfig
import com.megogo.assignment.http.models.FindRequest
import com.megogo.assignment.modules.ServiceModule
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.io._
import org.http4s.server.middleware._
import org.http4s.{HttpApp, HttpRoutes}

object Routes {
  def routes(serviceModule: ServiceModule): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case req @ POST -> Root / "find" =>
        for {
          request <- req.as[FindRequest]
          result  <- serviceModule.findFacade.process(request).toResponse(Ok(_))
        } yield result
    }

  def middleware(httpConfig: HttpConfig): HttpApp[IO] => IO[HttpApp[IO]] = { http: HttpApp[IO] =>
    Timeout(httpConfig.requestTimeout)(http)
  }.andThen { http: HttpApp[IO] =>
      RequestLogger.httpApp(logHeaders = false, logBody = false)(http)
    }
    .andThen { http: HttpApp[IO] =>
      ResponseLogger.httpApp(logHeaders = false, logBody = false)(http)
    }
    .andThen { http: HttpApp[IO] =>
      Throttle(httpConfig.rateLimit.amountRequests, httpConfig.rateLimit.per)(http)
    }
}
