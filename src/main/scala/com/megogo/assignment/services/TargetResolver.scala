package com.megogo.assignment.services

import cats.effect._
import com.megogo.assignment.config.AppConfig
import io.circe.Json
import org.http4s._
import org.http4s.circe._
import org.http4s.client._
import org.http4s.client.blaze._
import pureconfig._
import pureconfig.generic.auto._

trait TargetResolver {
  def resolve(optTarget: Option[Int]): IO[Int]
}

object TargetResolver {
  def make(client: Client[IO], config: AppConfig): TargetResolver = new TargetResolver {
    override def resolve(optTarget: Option[Int]): IO[Int] = {
      val uri = optTarget match {
        case Some(t) => Uri.unsafeFromString(s"https://httpbin.org/get?target=$t")
        case None => Uri.unsafeFromString(s"https://httpbin.org/get")
      }

      client.expect[Json](uri).map { json =>
        json.hcursor.downField("args").get[String]("target").toOption
          .flatMap(_.toIntOption)
          .getOrElse(config.defaultTarget)
      }.handleError(_ => config.defaultTarget)
    }
  }
}
