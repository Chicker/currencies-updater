package ru.chicker.interpreters

import cats.effect.IO
import ru.chicker.algebras
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import ru.chicker.algebras.CurrencyExchangeRateDSL

import java.util.Currency

object CurrencyExchangeInterpreter {
  implicit object CurrencyExchangeRateInter extends algebras.CurrencyExchangeRateDSL[IO] {
    override def load(baseCurrency: Currency): IO[Map[Currency, BigDecimal]] = {
      IO.pure {
        Map(
          Currency.getInstance("EUR") -> 0.23,
          Currency.getInstance("PLN") -> 1.89
        )
      }
    }
  }
}

class CurrencyExchangeTest extends AnyFunSpec with Matchers {
  describe("") {
    import CurrencyExchangeInterpreter._
    it("") {
      val exchangeRateInterp = CurrencyExchangeRateDSL[IO]
      val baseCurrency = Currency.getInstance("USD")
      exchangeRateInterp.load(baseCurrency).unsafeRunSync().get(Currency.getInstance("PLN")) shouldEqual Some(1.89)
    }
  }
}
