package com.megogo.assignment.services

import cats.implicits._

trait SumPairService {
  def findPairs(data: List[Int], target: Int): List[(Int, Int, Int, Int)]
}

object SumPairService {
  def make: SumPairService = new SumPairService {
    override def findPairs(data: List[Int], target: Int): List[(Int, Int, Int, Int)] =
      (for {
        (x, i) <- data.zipWithIndex
        (y, j) <- data.zipWithIndex if i < j && x + y == target
      } yield (i, j, x, y))
  }
}
