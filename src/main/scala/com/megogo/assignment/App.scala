package com.megogo.assignment

import cats.effect._
import com.megogo.assignment.config.AppConfig
import com.megogo.assignment.http.{FindRoutes, Server}
import com.megogo.assignment.services.{SumPairService, TargetResolver}
import org.http4s.blaze.client.BlazeClientBuilder
import org.typelevel.log4cats.slf4j.Slf4jLogger

object App extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val appConfig = AppConfig.load()
    val logger = Slf4jLogger.getLogger[IO]

    BlazeClientBuilder[IO].resource.use { client =>
      val sumService     = SumPairService.make
      val targetResolver = TargetResolver.make(client, appConfig)
      val httpApp        = FindRoutes.routes(sumService, targetResolver).orNotFound

      Server.serve(httpApp, appConfig.http).as(ExitCode.Success)

    }
  }
}
