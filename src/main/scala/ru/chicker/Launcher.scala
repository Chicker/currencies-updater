package ru.chicker

import cats.Monad
import cats.effect.IO
import cats.implicits._
import ru.chicker.algebras.{CurrencyExchangeRateDSL, CurrencyExchangeRateRepo}

import java.util.Currency

object Launcher extends App {
  import interpreters._

  private lazy val USD = Currency.getInstance("USD")

  def program[F[_]: Monad: CurrencyExchangeRateDSL: CurrencyExchangeRateRepo](baseCurrency: Currency): F[Unit] = {
    for {
      exchangeRates <- CurrencyExchangeRateDSL[F].load(baseCurrency)
      _ <- CurrencyExchangeRateRepo[F].update(exchangeRates)
    } yield ()
  }

  program[IO](USD).unsafeRunSync()
}


