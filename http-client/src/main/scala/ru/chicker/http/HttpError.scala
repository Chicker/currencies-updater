package ru.chicker.http

sealed trait HttpError extends Throwable {
  def message: String
  def code: HttpErrorCode
}

sealed trait HttpErrorCode extends Product with Serializable

object HttpErrorCode {
  final case object GeneralHttpError extends HttpErrorCode
}

object HttpError {

  final case class GeneralHttpError(override val message: String) extends HttpError {
    def code: HttpErrorCode = HttpErrorCode.GeneralHttpError
  }
}
