package ru.chicker.data

import java.time.OffsetDateTime
import java.util.Currency

final case class ExchangeRateModel(base: Currency,
                                   date: OffsetDateTime,
                                   rates: Map[Currency, BigDecimal])
