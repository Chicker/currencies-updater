package ru.chicker.data

import java.time.LocalDate
import java.util.Currency

final case class ExchangeRate(from: Currency, to: Currency, value: BigDecimal, updated: Option[LocalDate])
