package com.megogo.assignment.services

import cats.effect.IO
import cats.implicits.toFoldableOps
import com.megogo.assignment.errors.ValidationError
import com.megogo.assignment.errors.ValidationError.{
  DataOutOfRange,
  DataTooLargeError,
  EmptyDataError,
  TargetOutOfRange
}
import com.megogo.assignment.http.models.{ FindRequest, FindResponse }

trait FindPairsService {
  def process(request: FindRequest): IO[FindResponse]
}

object FindPairsService {
  def make(resolver: TargetResolverService, sumService: PairService): FindPairsService =
    new FindPairsService {
      override def process(request: FindRequest): IO[FindResponse] =
        for {
          _      <- validate(request)
          target <- resolver.resolve(request.target)
          pairs  <- sumService.findPairs(request.data, target)
        } yield FindResponse(target, pairs)

      // todo if growth move to separate validation class
      private def validate(req: FindRequest) = {
        val Min = -1_000_000_000
        val Max =  1_000_000_000

        def outOfRange(x: Int): Boolean = x < Min || x > Max

        List(
          Option.when(req.data.length < 2)(EmptyDataError),
          Option.when(req.data.length > 10000)(DataTooLargeError),
          Option.when(req.data.exists(outOfRange))(DataOutOfRange),
          Option.when(req.target.exists(outOfRange))(TargetOutOfRange)
        ).collectFirstSome(identity)
          .fold(IO.unit)(IO.raiseError)
      }
    }
}
