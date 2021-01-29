package ru.chicker.algebras

import cats.Monad
import cats.implicits._
import ru.chicker.data.ExchangeRate
import ru.chicker.db
import cats.effect.Bracket
import tofu.logging.Logging
import tofu.syntax.logging._

trait CurrencyExchangeRateRepo[F[_]] {
  def update(rates: Seq[ExchangeRate]): F[Unit]
}

object CurrencyExchangeRateRepo {
  import doobie._

  def apply[F[_]](implicit instance: CurrencyExchangeRateRepo[F]): CurrencyExchangeRateRepo[F] = instance

  def fromDoobie[F[_]: Logging](xa: Transactor[F])(implicit br: Bracket[F, Throwable]): CurrencyExchangeRateRepo[F] =
    new Impl(xa)

  private final class Impl[F[_]: Monad: Logging](xa: Transactor[F])(implicit br: Bracket[F, Throwable])
      extends CurrencyExchangeRateRepo[F] {
    import doobie.implicits._

    override def update(rates: Seq[ExchangeRate]): F[Unit] = {
      debug"Updating... first entry: ${rates.headOption.fold("")(v => s"currency is $v")}" *>
        db.Queries.storeRates(rates).transact(xa).map(_ => ())
    }
  }
}
