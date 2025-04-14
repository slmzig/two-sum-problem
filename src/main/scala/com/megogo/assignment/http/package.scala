package com.megogo.assignment

import cats.effect.IO
import com.megogo.assignment.errors.ValidationError
import com.megogo.assignment.http.models.ErrorResponse
import io.circe.syntax.EncoderOps
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{Response, Status}

package object http {

  implicit class IOAppErrorOps[A](val result: IO[A]) extends AnyVal {

    def toResponse(onSuccess: A => IO[Response[IO]]): IO[Response[IO]] = {
      def onError(error: Throwable): IO[Response[IO]] = error match {
        case e: ValidationError =>
          BadRequest(ErrorResponse(e.message, Status.BadRequest.code).asJson)
        case _ =>
          InternalServerError(ErrorResponse("Unexpected error", Status.InternalServerError.code).asJson)
      }

      result.attempt.flatMap(_.fold(onError, onSuccess))
    }
  }
}
