import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import java.time.LocalDate
import java.util.Currency
import ru.chicker.data.ExchangeRateResp

class CirceJsonTest extends AnyFunSpec with Matchers {
    describe("json") {
        it("should correct deserialize latest response") {
            import ru.chicker.serialization.json._

            val json = """
            |{
            |  "base": "EUR",
            |  "date": "2018-04-08",
            |  "rates": {
            |    "CAD": 1.565,
            |    "CHF": 1.1798,
            |    "GBP": 0.87295,
            |    "SEK": 10.2983,
            |    "EUR": 1.092,
            |    "USD": 1.2234
            |  }
            |}
            """.stripMargin
            val parsed: io.circe.Json = io.circe.parser.parse(json).fold(e => fail(e.getMessage), identity)

            val model = parsed.as[ExchangeRateResp].fold(e => fail(e.getMessage), identity)

            model.base shouldEqual Currency.getInstance("EUR")
            model.date shouldEqual LocalDate.of(2018, 4, 8)
            model.rates.size shouldEqual 6
        }
    }
}