package com.megogo.assignment.services

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

final case class Pair(indices: (Int, Int), numbers: (Int, Int))

object Pair {
  implicit val decoder: Decoder[Pair] = deriveDecoder
  implicit val encoder: Encoder[Pair] = deriveEncoder
}