package com.megogo.assignment.http.models

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

final case class FindRequest(data: List[Int], target: Option[Int])

object FindRequest {
  implicit val decoder: Decoder[FindRequest] = deriveDecoder
  implicit val encoder: Encoder[FindRequest] = deriveEncoder
}



