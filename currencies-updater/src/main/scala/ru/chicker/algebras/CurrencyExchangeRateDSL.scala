package ru.chicker.algebras

import java.time.LocalDate
import java.util.Currency

import cats.Monad
import cats.data.NonEmptyList
import cats.effect.Concurrent
import cats.implicits._
import ru.chicker.data.ExchangeRate
import ru.chicker.data.ExchangeRateResp
import ru.chicker.http.HttpClient
import ru.chicker.services._
import ru.chicker.tofuext.RaceExt
import tofu.logging.Logging

trait CurrencyExchangeRateDSL[F[_]] {
  def load(baseCurrency: Currency): F[Seq[ExchangeRate]]
}

object CurrencyExchangeRateDSL {
  def apply[F[_]](implicit instance: CurrencyExchangeRateDSL[F]): CurrencyExchangeRateDSL[F] = instance

  def usingRemoteApi[F[_]: Monad: Logging: Concurrent: HttpClient: RaceExt]: CurrencyExchangeRateDSL[F] = new Impl[F]()

  private final class Impl[F[_]: Monad: Logging: HttpClient: RaceExt: Concurrent]() extends CurrencyExchangeRateDSL[F] {

    private val rateApi1 = ExchangeRateApi[F]
    private val rateApi2 = RatesApi[F]
    private val apis: NonEmptyList[AbstractExchangeRateApi[F]] = NonEmptyList.of(rateApi1, rateApi2)

    def getLatestCurrencies(baseCurrency: Currency): F[ExchangeRateResp] = {
      val results = apis.map(_.latest(baseCurrency))

      implicitly[RaceExt[F]].raceManyIgnoreErrors(results.toIterable)
    }

    def modelToMap(model: ExchangeRateResp): Seq[ExchangeRate] = {
      model.rates.toList.map { case (curr, amount) =>
        ExchangeRate(
          from = model.base,
          to = curr,
          value = amount,
          updated = Some(LocalDate.now)
        )
      }
    }

    override def load(baseCurrency: Currency): F[Seq[ExchangeRate]] = {
      getLatestCurrencies(baseCurrency).map(modelToMap)
    }
  }
}
