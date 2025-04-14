package com.megogo.assignment.services

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.implicits.none
import com.megogo.assignment.config.TargetResolverConfig
import com.megogo.assignment.util.{ CacheFactory, LogFactory }
import io.circe.Json
import org.http4s.EntityDecoder
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax
import org.mockito.cats.IdiomaticMockitoCats
import org.mockito.{ ArgumentMatchersSugar, IdiomaticStubbing }
import org.scalatest.OneInstancePerTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.duration.DurationInt

class TargetResolverTest
    extends AsyncWordSpec
    with AsyncIOSpec
    with OneInstancePerTest
    with ArgumentMatchersSugar
    with IdiomaticStubbing
    with IdiomaticMockitoCats
    with Matchers {

  val defaultTarget = 10
  val config        = TargetResolverConfig(baseUri = uri"https://httpbin.org", defaultTarget = defaultTarget)
  val client        = mock[Client[IO]]

  val initService = for {
    cache    <- CacheFactory.make[IO](60.minutes)
    logger   <- LogFactory.make()
    resolver <- TargetResolverService.make(client, cache, logger, config)
  } yield resolver

  "TargetResolverService" should {
      "call correct URI when targetOpt is Some" in {
        val expectedUri = uri"https://httpbin.org/get?target=123"
        val json        = Json.obj("args" -> Json.obj("target" -> Json.fromString("123")))

        client.expect[Json](expectedUri)(any[EntityDecoder[IO, Json]]).returnsF(json)

        for {
          service <- initService
          result  <- service.resolve(Some(123))
        } yield result shouldBe 123
      }

      "call correct URI when targetOpt is None" in {
        val expectedUri = uri"https://httpbin.org/get"
        val json        = Json.obj("args" -> Json.Null)

        client.expect[Json](expectedUri)(any[EntityDecoder[IO, Json]]).returnsF(json)

        for {
          service <- initService
          result  <- service.resolve(none)
        } yield result shouldBe defaultTarget
      }

      "fallback to defaultTarget if parsing fails" in {
        val json        = Json.obj("args" -> Json.obj("target" -> Json.fromString("not-a-number")))
        val expectedUri = uri"https://httpbin.org/get"

        client.expect[Json](expectedUri)(any[EntityDecoder[IO, Json]]).returnsF(json)

        for {
          service <- initService
          result  <- service.resolve(none)
        } yield result shouldBe defaultTarget
      }

      "fallback to defaultTarget on HTTP error" in {
        val expectedUri = uri"https://httpbin.org/get"

        client
          .expect[Json](expectedUri)(any[EntityDecoder[IO, Json]])
          .returns(IO.raiseError(new RuntimeException("error")))

        for {
          service <- initService
          result  <- service.resolve(none)
        } yield result shouldBe defaultTarget
      }
    }
}
