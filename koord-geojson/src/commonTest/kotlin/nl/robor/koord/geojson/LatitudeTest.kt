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
import nl.robor.koord.units.Angle.Companion.degrees

class LatitudeTest :
    FunSpec({
        test("in valid range [-90, 90]") {
            Exhaustive
                .ints(-90..90)
                .map { it.toDouble().degrees }
                .map(Latitude::invoke)
                .forAll { value -> value.isRight() }
        }

        test("outside valid range") {
            val arbInvalidNegative = Arb.double(Double.NEGATIVE_INFINITY, -90.0).filterNot { it == -90.0 }
            val arbInvalidPositive = Arb.double(90.0, Double.POSITIVE_INFINITY).filterNot { it == 90.0 }
            arbInvalidNegative
                .merge(arbInvalidPositive)
                .forAll { Latitude(it.degrees).isLeft() }
        }

        test("encode") {
            val latitude: Latitude = Latitude((-90.0).degrees).shouldBeRight()
            val encodedLatitude = Json.encodeToString(latitude)
            encodedLatitude shouldEqualJson { "-90.0" }
        }

        test("decode valid value") {
            val encoded = "-90.0"
            val decoded: Latitude = Json.decodeFromString(encoded)
            decoded.angle shouldBeEqual (-90.0).degrees
        }

        test("decode invalid value") {
            listOf(
                "-91.0",
                "91.0",
            ).forEach { encodedValue ->
                shouldThrow<ValidationResult.Exception> {
                    Json.decodeFromString<Latitude>(encodedValue)
                }
            }
        }
    })
