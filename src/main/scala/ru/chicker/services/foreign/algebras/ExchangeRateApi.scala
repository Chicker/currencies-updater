package ru.chicker.services.foreign.algebras

import cats.Monad
import cats.implicits._

import ru.chicker.data.{AppError, ExchangeRateModel}
import ru.chicker.services.ExchangeRateAbstractApi

import java.util.Currency

class ExchangeRateApi[F[_]: Monad] extends ExchangeRateAbstractApi[F] {
  override def latest(baseCurrency: Currency): F[Either[AppError, ExchangeRateModel]] = {
    for {
      model <-
    } yield model.right
  }

  private def makeRequest(baseCurrency: Currency): F[]
}

