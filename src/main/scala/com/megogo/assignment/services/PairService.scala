package com.megogo.assignment.services

import cats.implicits._
import cats.effect._
import com.megogo.assignment.errors.CommonError
import com.megogo.assignment.util.LogFactory

trait PairService {
  def findPairs(data: List[Int], target: Int): IO[List[Pair]]
}

object PairService {
  def make(logger: LogFactory): IO[PairService] =
    logger.newLog(getClass).map { log =>
      new PairService {
        override def findPairs(data: List[Int], target: Int): IO[List[Pair]] = {
          val initial = (Map.empty[Int, List[Int]], List.empty[Pair])

          val (_, pairs) = data.zipWithIndex.foldLeft(initial) {
            case ((seen, found), (current, idx)) =>
              val complement = target - current

              val newPairs = seen.getOrElse(complement, Nil).map { j =>
                Pair((j, idx), (complement, current))
              }

              val updatedSeen = seen.updatedWith(current) {
                case Some(indices) => Some(indices :+ idx) // for duplicated values
                case None          => Some(List(idx))
              }

              (updatedSeen, found ++ newPairs)
          }

          pairs
        }.pure[IO].handleErrorWith { error =>
          log.error(error)(s"Failed to find pairs") *> CommonError(error.getMessage).raiseError[IO, List[Pair]]
        }
      }
    }
}
