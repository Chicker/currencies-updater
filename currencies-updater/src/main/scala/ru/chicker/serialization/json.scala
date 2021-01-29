package ru.chicker.serialization

import io.circe._
import io.circe.generic.semiauto._

import ru.chicker.data.ExchangeRateResp
import java.util.Currency

trait JsonSerializer {
  implicit val currencyDecoder: Decoder[Currency] = Decoder[String].map(Currency.getInstance)
  implicit val currencyKeyDecoder: KeyDecoder[Currency] = KeyDecoder[String].map(Currency.getInstance)

  implicit val exchangeRateModelDecoder: Decoder[ExchangeRateResp] = deriveDecoder[ExchangeRateResp]
}

object json extends JsonSerializer
