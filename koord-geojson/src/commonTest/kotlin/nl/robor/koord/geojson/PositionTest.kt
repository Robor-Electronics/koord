package nl.robor.koord.geojson

import arrow.core.Some
import dev.nesk.akkurate.ValidationResult
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.single
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.robor.koord.units.Angle.Companion.degrees

class PositionTest :
    FunSpec({
        context("encode") {
            test("without altitude") {
                val position =
                    Position(
                        longitude = arbLongitude.single(),
                        latitude = arbLatitude.single(),
                    )
                val encoded = Json.encodeToString(position)
                encoded shouldEqualJson {
                    """[${position.longitude.angle.inDegrees}, ${position.latitude.angle.inDegrees}]"""
                }
            }

            test("with altitude") {
                val position =
                    Position(
                        longitude = arbLongitude.single(),
                        latitude = arbLatitude.single(),
                        altitude = Some(100.0),
                    )
                val encoded = Json.encodeToString(position)
                encoded shouldEqualJson {
                    """[${position.longitude.angle.inDegrees}, ${position.latitude.angle.inDegrees}, ${position.altitude.shouldBeSome()}]"""
                }
            }
        }

        context("decode") {
            test("without altitude") {
                val longitude = arbLongitude.single()
                val latitude = arbLatitude.single()
                val decoded = """[${longitude.angle.inDegrees}, ${latitude.angle.inDegrees}]"""
                val position: Position = Json.decodeFromString(decoded)
                position.coordinates shouldHaveSize 2
                position.longitude shouldBeEqual longitude
                position.latitude shouldBeEqual latitude
            }

            test("with altitude") {
                val longitude = arbLongitude.single()
                val latitude = arbLatitude.single()
                val altitude = Arb.double().single()
                val decoded = """[${longitude.angle.inDegrees}, ${latitude.angle.inDegrees}, $altitude]"""
                val position: Position = Json.decodeFromString(decoded)
                position.coordinates shouldHaveSize 3
                position.longitude shouldBeEqual longitude
                position.latitude shouldBeEqual latitude
                position.altitude shouldBeSome altitude
            }
        }

        context("validation") {
            test("invalid lon/lat") {
                val longitude = Longitude(180.0.degrees).shouldBeRight()
                val latitude = Latitude(90.0.degrees).shouldBeRight()
                // Latitude / longitude swapped in encoded json
                val json = """[${latitude.angle.inDegrees}, ${longitude.angle.inDegrees}]"""
                shouldThrow<ValidationResult.Exception> {
                    Json.decodeFromString<Position>(json)
                }
            }
        }
    })
