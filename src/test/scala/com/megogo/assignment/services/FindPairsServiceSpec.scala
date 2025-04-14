package com.megogo.assignment.services

import cats.effect.testing.scalatest.AsyncIOSpec
import cats.implicits.catsSyntaxEitherId
import com.megogo.assignment.errors.ValidationError.{DataOutOfRange, DataTooLargeError, EmptyDataError, TargetOutOfRange}
import com.megogo.assignment.http.models.FindRequest
import org.mockito.cats.IdiomaticMockitoCats
import org.mockito.{ArgumentMatchersSugar, IdiomaticStubbing}
import org.scalatest.OneInstancePerTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

class FindPairsServiceSpec
    extends AsyncWordSpec
    with AsyncIOSpec
    with OneInstancePerTest
    with ArgumentMatchersSugar
    with IdiomaticStubbing
    with IdiomaticMockitoCats
    with Matchers {

  "FindFacadeService" should {

    val targetResolverService = mock[TargetResolverService]
    val sumPairService        = mock[PairService]
    val facade                = FindPairsService.make(targetResolverService, sumPairService)

    "return correct FindResponse for valid input" in {

      val request = FindRequest(data = List(8, 10), target = Some(18))

      targetResolverService.resolve(Option(18)).returnsF(18)
      sumPairService.findPairs(request.data, 18).returnsF(List(Pair((0, 1), (8, 10))))

      facade.process(request).asserting { response =>
        response.target shouldBe 18
        response.pairs should contain only Pair((0, 1), (8, 10))
      }
    }

    "fail if data is empty" in {
      val request = FindRequest(data = Nil, target = Some(5))

      facade
        .process(request)
        .attempt
        .map(_ shouldBe EmptyDataError.asLeft)
    }

    "fail if data is less then two" in {
      val request = FindRequest(data = List(5), target = Some(5))

      facade
        .process(request)
        .attempt
        .map(_ shouldBe EmptyDataError.asLeft)
    }

    "fail if data is too large" in {
      val request = FindRequest(data = List.fill(10001)(1), target = Some(5))

      facade
        .process(request)
        .attempt
        .map(_ shouldBe DataTooLargeError.asLeft)
    }

    "fail if data contains number out of range" in {
      val request = FindRequest(data = List(1, 2, Int.MaxValue), target = Some(5))

      facade
        .process(request)
        .attempt
        .map(_ shouldBe DataOutOfRange.asLeft)
    }

    "fail if target is out of range" in {
      val request = FindRequest(data = List(1, 2), target = Some(Int.MaxValue))

      facade
        .process(request)
        .attempt
        .map(_ shouldBe TargetOutOfRange.asLeft)
    }
  }
}
