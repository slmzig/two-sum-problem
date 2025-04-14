package com.megogo.assignment.util

import cats.effect.Sync
import cats.implicits.toFlatMapOps
import com.github.benmanes.caffeine.cache.{Caffeine, Cache => JavaCache}

import java.time.{Duration => JDuration}
import scala.concurrent.duration.Duration

trait Cache[F[_], K, V] {
  def getOrUpdate(key: K)(fetch: => F[V]): F[V]
}

trait CacheFactory[F[_]] {
  def newCache[K, V]: F[Cache[F, K, V]]
}

object CacheFactory {
  def make[F[_]: Sync](ttl: Duration): F[CacheFactory[F]] = Sync[F].pure(
    new CacheFactory[F] {
      override def newCache[K, V]: F[Cache[F, K, V]] = Sync[F].delay {
        val caffeine: JavaCache[K, V] =
          Caffeine.newBuilder()
            .expireAfterWrite(JDuration.ofMillis(ttl.toMillis))
            .build[K, V]()

        new Cache[F, K, V] {
          override def getOrUpdate(key: K)(fetch: => F[V]): F[V] = {
            Option(caffeine.getIfPresent(key)) match {
              case Some(value) => Sync[F].delay(value)
              case None =>
                fetch.flatTap(value => Sync[F].delay(caffeine.put(key, value)))
            }
          }
        }
      }
    }
  )
}

