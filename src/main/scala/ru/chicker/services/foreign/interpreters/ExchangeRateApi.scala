package ru.chicker.services.foreign.interpreters

import cats.effect.IO
import ru.chicker.services.foreign.algebras.ExchangeRateApi

object ExchangeRateApi {
 implicit object ExchangeRateApiInterp extends ExchangeRateApi[IO]
}
