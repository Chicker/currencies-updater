package ru.chicker.services

import ru.chicker.data.{AppError, ExchangeRateModel}

import java.util.Currency

trait ExchangeRateAbstractApi[F[_]] {
  def latest(baseCurrency: Currency): F[Either[AppError, ExchangeRateModel]]
}
