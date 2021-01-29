package ru.chicker.serialization

import java.util.Currency
import doobie._
// import doobie.postgres._

trait DoobieInstances {
  import doobie.implicits.javatime.JavaTimeLocalDateMeta

  implicit val localDateMeta = JavaTimeLocalDateMeta

  implicit val currencyPut: Put[Currency] = Put[String].contramap(_.getCurrencyCode())
}

object doobiextra extends DoobieInstances
