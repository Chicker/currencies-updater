package ru.chicker.services

import java.util.Currency
import ru.chicker.data.ExchangeRateResp

trait AbstractExchangeRateApi[F[_]] {
  def latest(baseCurrency: Currency): F[ExchangeRateResp]
}
