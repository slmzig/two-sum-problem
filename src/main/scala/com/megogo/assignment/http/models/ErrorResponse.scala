package com.megogo.assignment.http.models

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

final case class ErrorResponse(
    message: String,
    status: Int,
    fields: Map[String, String] = Map.empty
)

object ErrorResponse {
  implicit val decoder: Decoder[ErrorResponse] = deriveDecoder
  implicit val encoder: Encoder[ErrorResponse] = deriveEncoder
}
