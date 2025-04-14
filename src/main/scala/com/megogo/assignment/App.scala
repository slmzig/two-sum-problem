package com.megogo.assignment

import cats.effect._
import com.megogo.assignment.config.AppConfig
import com.megogo.assignment.http.{Routes, Server}
import com.megogo.assignment.modules.ServiceModule
import com.megogo.assignment.util.LogFactory
import org.http4s.blaze.client.BlazeClientBuilder

object App extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val appConfig = AppConfig.load()

    BlazeClientBuilder[IO].resource.use { client =>
      for {
        logger        <- LogFactory.make()
        serviceModule <- ServiceModule.make(client, appConfig, logger)
        baseRoutes    = Routes.routes(serviceModule).orNotFound
        http          <- Routes.middleware(appConfig.http)(baseRoutes)
        server        <- Server.serve(http, appConfig.http).as(ExitCode.Success)
      } yield server
    }
  }
}
