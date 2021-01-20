package ru.chicker

import cats.effect.IO
import ru.chicker.algebras.{CurrencyExchangeRateDSL, CurrencyExchangeRateRepo}

import java.util.Currency

package object interpreters {
  implicit object FakeCurrencyExchangeRate extends CurrencyExchangeRateDSL[IO] {
    override def load(baseCurrency: Currency): IO[Map[Currency, BigDecimal]] = {
      IO.pure {
        Map(
          Currency.getInstance("EUR") -> 0.23,
          Currency.getInstance("PLN") -> 1.89
        )
      }
    }
  }

  implicit object CurrencyExchangeRateRepoInter extends CurrencyExchangeRateRepo[IO] {
    override def update(rates: Map[Currency, BigDecimal]): IO[Unit] = {
      IO.delay {
        rates.foreachEntry(printEntry)
      }
    }

    private def printEntry(currency: Currency, rate: BigDecimal): Unit = {
      println(s"Currency [${currency.getCurrencyCode}] has rate $rate")
    }
  }
}
