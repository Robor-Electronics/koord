package nl.robor.koord.geojson

import dev.nesk.akkurate.ValidationResult
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.merge
import io.kotest.property.exhaustive.ints
import io.kotest.property.exhaustive.map
import io.kotest.property.forAll
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LongitudeTest :
    FunSpec({
        test("in valid range [-180, 180]") {
            Exhaustive
                .ints(-180..180)
                .map { it.toDouble() }
                .map(Longitude::invoke)
                .forAll { value -> value.isRight() }
        }

        test("outside valid range") {
            val arbInvalidNegative = Arb.double(Double.NEGATIVE_INFINITY, -180.0).filterNot { it == -180.0 }
            val arbInvalidPositive = Arb.double(180.0, Double.POSITIVE_INFINITY).filterNot { it == 180.0 }
            arbInvalidNegative
                .merge(arbInvalidPositive)
                .forAll { Longitude(it).isLeft() }
        }

        test("encode") {
            val longitude: Longitude = Longitude(-180.0).shouldBeRight()
            val encodedLongitude = Json.encodeToString(longitude)
            encodedLongitude shouldEqualJson { "-180.0" }
        }

        test("decode valid value") {
            val encoded = "-180.0"
            val decoded: Longitude = Json.decodeFromString(encoded)
            decoded.degrees shouldBeEqual (-180.0).degrees
        }

        test("decode invalid value") {
            listOf(
                "-181.0",
                "181.0",
            ).forEach { encodedValue ->
                shouldThrow<ValidationResult.Exception> {
                    Json.decodeFromString<Longitude>(encodedValue)
                }
            }
        }
    })
