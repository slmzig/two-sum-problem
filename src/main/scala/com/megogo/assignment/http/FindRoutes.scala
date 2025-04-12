package com.megogo.assignment.http

import cats.effect._
import com.megogo.assignment.http.models.{FindRequest, FindResponse}
import com.megogo.assignment.services.{Pair, SumPairService, TargetResolver}
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.io._

object FindRoutes {
  def routes(sumService: SumPairService, targetResolver: TargetResolver): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case req @ POST -> Root / "find" =>
        for {
          findReq  <- req.as[FindRequest]
          target   <- targetResolver.resolve(findReq.target)
          pairsRaw = sumService.findPairs(findReq.data, target)
          pairs    = pairsRaw.map { case (i, j, x, y) => Pair((i, j), (x, y)) }
          resp     <- Ok(FindResponse(target, pairs))
        } yield resp
    }
}
