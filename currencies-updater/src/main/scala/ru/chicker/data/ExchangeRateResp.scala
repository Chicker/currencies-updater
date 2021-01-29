package ru.chicker.data

import java.util.Currency
import java.time.LocalDate

final case class ExchangeRateResp(base: Currency, date: LocalDate, rates: Map[Currency, BigDecimal])
