package com.megogo.assignment.config

import org.http4s.Uri

final case class TargetResolverConfig(
    baseUri: Uri,
    defaultTarget: Int
)
