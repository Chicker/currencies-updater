package ru.chicker.algebras

import java.util.Currency

trait CurrencyExchangeRateRepo[F[_]] {
  def update(rates: Map[Currency, BigDecimal]): F[Unit]
}

object CurrencyExchangeRateRepo {
  def apply[F[_]](implicit instance: CurrencyExchangeRateRepo[F]): CurrencyExchangeRateRepo[F] = instance
}
