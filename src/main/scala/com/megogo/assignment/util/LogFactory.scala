package com.megogo.assignment.util

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait LogFactory {
  def newLog(clazz: Class[_]): IO[Logger[IO]]
}

object LogFactory {
  def make(): IO[LogFactory] =
    new LogFactory {
      override def newLog(clazz: Class[_]): IO[Logger[IO]] = IO.delay(Slf4jLogger.getLoggerFromClass(clazz))
    }.pure[IO]
}
