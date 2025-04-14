package com.megogo.assignment.modules

import cats.effect.IO
import com.megogo.assignment.config.AppConfig
import com.megogo.assignment.services.{ FindPairsService, PairService, TargetResolverService }
import com.megogo.assignment.util.{ CacheFactory, LogFactory }
import org.http4s.client.Client

final case class ServiceModule(
  sumService: PairService,
  targetResolverService: TargetResolverService,
  findPairsService: FindPairsService
)

object ServiceModule {
  def make(client: Client[IO], config: AppConfig, logger: LogFactory): IO[ServiceModule] =
    for {
      cache            <- CacheFactory.make[IO](config.cache.ttl)
      targetResolver   <- TargetResolverService.make(client, cache, logger, config.targetResolver)
      sumPairsService  <- PairService.make(logger)
      findPairsService = FindPairsService.make(targetResolver, sumPairsService)
    } yield ServiceModule(sumPairsService, targetResolver, findPairsService)
}
