package com.megogo.assignment.services

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import com.megogo.assignment.util.LogFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

class PairServiceTest extends AsyncWordSpec with AsyncIOSpec with Matchers {

  val initService: IO[PairService] = LogFactory.make().flatMap(PairService.make)

  "SumPairService" should {
      "return one correct pair" in {
        for {
          service <- initService
          result  <- service.findPairs(List(3, 8, 10, 14), 18)
        } yield result should contain only Pair((1, 2), (8, 10))
      }

      "return multiple pairs if available" in {
        for {
          service <- initService
          result  <- service.findPairs(List(1, 2, 3, 4), 5)
        } yield (result should contain).allOf(Pair((0, 3), (1, 4)), Pair((1, 2), (2, 3)))
      }

      "return empty when no matching pairs found" in {
        for {
          service <- initService
          result  <- service.findPairs(List(1, 1, 1), 10)
        } yield result shouldBe empty
      }

      "should return duplicate pairs" in {
        for {
          service <- initService
          result  <- service.findPairs(List(2, 2, 3, 5), 5)
        } yield (result should contain).allOf(Pair((0, 2), (2, 3)), Pair((1, 2), (2, 3)))
      }
    }
}
