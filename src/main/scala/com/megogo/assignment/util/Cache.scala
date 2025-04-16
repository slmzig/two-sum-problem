package com.megogo.assignment.util

import cats.effect._
import cats.effect.implicits.asyncOps
import cats.implicits._
import com.github.benmanes.caffeine.cache.{ Caffeine, Cache => JavaCache }

import java.time.{ Duration => JDuration }
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import java.util.concurrent.Executors

trait Cache[F[_], K, V] {
  def getOrUpdate(key: K)(fetch: => F[V]): F[V]
}

trait CacheFactory[F[_]] {
  def newCache[K, V]: F[Cache[F, K, V]]
}

object CacheFactory {
  def make[F[_]: Async](ttl: Duration): F[CacheFactory[F]] = {

    val cacheEC = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

    Async[F].pure(new CacheFactory[F] {
      override def newCache[K, V]: F[Cache[F, K, V]] =
        Async[F].delay {
          val caffeine: JavaCache[K, V] = Caffeine
            .newBuilder()
            .expireAfterWrite(JDuration.ofMillis(ttl.toMillis))
            .build[K, V]()

          new Cache[F, K, V] {
            override def getOrUpdate(key: K)(fetch: => F[V]): F[V] =
              Async[F].defer {
                Option(caffeine.getIfPresent(key)) match {
                  case Some(value) =>
                    Async[F].pure(value).evalOn(cacheEC)
                  case None =>
                    fetch.flatTap { value =>
                      Async[F].delay(caffeine.put(key, value)).void.evalOn(cacheEC)
                    }
                }
              }
          }
        }
    })
  }
}
