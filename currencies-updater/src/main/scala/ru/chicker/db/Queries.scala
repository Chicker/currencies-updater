package ru.chicker.db

import doobie._
import doobie.implicits._
import ru.chicker.data.ExchangeRate

object Queries extends QueryUtils {

  lazy val exchangeRatesColumns = Seq(
    "exchange_from",
    "exchange_to",
    "rate",
    "updated"
  )

  def storeRates(rates: Seq[ExchangeRate]): ConnectionIO[Int] = {
    import ru.chicker.serialization.doobiextra._

    val q = rates.map { rate =>
      sql"INSERT INTO exchange_rates(" ++ columns(exchangeRatesColumns) ++ fr0")" ++
        fr"VALUES(${rate.to}, ${rate.from}, ${rate.value}, ${rate.updated})" ++
        sql"""ON CONFLICT ON CONSTRAINT exchange_rates_pkey DO UPDATE
           |  SET rate = ${rate.value}, updated = ${rate.updated};
           |""".stripMargin
    }.reduce[Fragment](_ ++ _)

    q.update.run
  }
}
