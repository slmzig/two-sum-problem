package com.megogo.assignment.services

import cats.effect._
import com.megogo.assignment.config.TargetResolverConfig
import com.megogo.assignment.util.{CacheFactory, LogFactory}
import io.circe.Json
import org.http4s.circe._
import org.http4s.client._

trait TargetResolverService {
  def resolve(targetOpt: Option[Int]): IO[Int]
}

object TargetResolverService {
  def make(
    client: Client[IO],
    cache: CacheFactory[IO],
    logger: LogFactory,
    config: TargetResolverConfig
  ): IO[TargetResolverService] = {

    for {
      log   <- logger.newLog(getClass)
      cache <- cache.newCache[Option[Int], Int]
    } yield {
      new TargetResolverService {
        override def resolve(targetOpt: Option[Int]): IO[Int] = {
          cache.getOrUpdate(targetOpt) {
            val uriWithPath = config.baseUri / "get"

            val uri = targetOpt match {
              case Some(t) => uriWithPath.withQueryParam("target", t.toString)
              case None    => uriWithPath
            }

            IO.blocking(client.expect[Json](uri))
              .flatten
              .map { json =>
                json.hcursor
                  .downField("args")
                  .get[String]("target")
                  .toOption
                  .flatMap(_.toIntOption)
                  .getOrElse(config.defaultTarget)
              }
              .handleErrorWith { err =>
                log.error(err)(s"Failed to resolve target from $uri, falling back to default: ${config.defaultTarget}") *>
                  IO.pure(config.defaultTarget)
              }
          }
        }
      }
    }
  }
}
