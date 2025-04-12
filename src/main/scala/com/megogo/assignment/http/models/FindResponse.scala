package com.megogo.assignment.http.models

import com.megogo.assignment.services.Pair
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

final case class FindResponse(target: Int, pairs: List[Pair])

object FindResponse {
  implicit val decoder: Decoder[FindResponse] = deriveDecoder
  implicit val encoder: Encoder[FindResponse] = deriveEncoder
}