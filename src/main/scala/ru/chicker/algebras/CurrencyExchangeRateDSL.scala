package ru.chicker.algebras

import java.util.Currency

trait CurrencyExchangeRateDSL[F[_]] {
  def load(baseCurrency: Currency): F[Map[Currency, BigDecimal]]
}

object CurrencyExchangeRateDSL {
  def apply[F[_]](implicit instance: CurrencyExchangeRateDSL[F]): CurrencyExchangeRateDSL[F] = instance
}
