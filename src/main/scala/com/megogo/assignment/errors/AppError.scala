package com.megogo.assignment.errors

sealed trait AppError extends Throwable {
  def message: String
}

sealed trait ValidationError extends AppError

object ValidationError {
  final case object DataTooLargeError extends ValidationError {
    val message = "Data must not exceed 10000 elements."
  }

  final case object EmptyDataError extends ValidationError {
    val message = "Data must contain at least 2 elements."
  }

  final case object DataOutOfRange extends ValidationError {
    val message = "Data must contain at least 2 elements."
  }

  final case object TargetOutOfRange extends ValidationError {
    val message = "Target must be in range [-1e9, 1e9]"
  }
}

case class CommonError(message: String) extends AppError
