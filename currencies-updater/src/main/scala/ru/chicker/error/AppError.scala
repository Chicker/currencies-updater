package ru.chicker.error

sealed trait AppError extends Throwable

object AppError {
  case class HttpError(m: String) extends AppError
}
