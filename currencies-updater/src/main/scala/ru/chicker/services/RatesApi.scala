package ru.chicker.services

import java.net.URI
import java.util.Currency

import cats.Monad
import ru.chicker.data._
import ru.chicker.http.HttpClient
import tofu.logging._

object RatesApi {
  def apply[F[_]: Monad: Logging: HttpClient]: AbstractExchangeRateApi[F] = new Impl()

  private final class Impl[F[_]: Monad: Logging: HttpClient] extends AbstractExchangeRateApi[F] {

    val httpClient = implicitly[HttpClient[F]]

    override def latest(baseCurrency: Currency): F[ExchangeRateResp] = {
      import ru.chicker.serialization.json._

      def uri(baseCurrency: Currency): URI =
        new URI(s"https://api.ratesapi.io/api/latest?base=${baseCurrency.getCurrencyCode}")

      httpClient.strictGet[ExchangeRateResp](uri(baseCurrency))
    }
  }
}
