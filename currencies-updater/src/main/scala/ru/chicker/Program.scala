package ru.chicker

import java.util.Currency

import cats._
import cats.effect._
import cats.syntax.flatMap._
import cats.syntax.functor._
import doobie._
import ru.chicker.algebras._
import ru.chicker.config.Config
import ru.chicker.data._
import ru.chicker.http.HttpClient
import ru.chicker.tofuext.RaceExt
import tofu.logging._

object Program {

  def apply[F[_]: Monad: ConcurrentEffect: ContextShift: Logging: RaceExt](
      httpClient: HttpClient[F],
      xa: Transactor[F],
      config: Config
  ): F[Unit] = {
    val baseCurrency = Currency.getInstance("USD")
    implicit val httpClientImpl = httpClient
    implicit val repo = CurrencyExchangeRateRepo.fromDoobie[F](xa)
    implicit val dsl = CurrencyExchangeRateDSL.usingRemoteApi[F]
    for {
      exchangeRates <- loadLatestRates[F](baseCurrency)
      _             <- storeRates(exchangeRates)
    } yield ()
  }

  private def loadLatestRates[F[_]: CurrencyExchangeRateDSL](
      baseCurrency: Currency
  ): F[Seq[ExchangeRate]] = {
    CurrencyExchangeRateDSL[F].load(baseCurrency)
  }

  private def storeRates[F[_]: CurrencyExchangeRateRepo](
      exchangeRates: Seq[ExchangeRate]
  ): F[Unit] = {
    CurrencyExchangeRateRepo[F].update(exchangeRates)
  }
}
