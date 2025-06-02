package nl.robor.koord.geojson

import arrow.core.None
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PointTest :
    FunSpec({
        val encodedPoint =
            """
            {
                 "type": "Point",
                 "coordinates": [100.0, 0.0]
            }
            """.trimIndent()

        test("encode") {
            val point: Geometry =
                Point(
                    coordinates =
                        Position(
                            longitude = Longitude(100.0).shouldBeRight(),
                            latitude = Latitude(0.0).shouldBeRight(),
                        ),
                    bbox = None,
                )

            val encoded = Json.encodeToString(point)
            encoded shouldEqualJson { encodedPoint }
        }

        test("decode") {
            val point: Geometry = Json.decodeFromString(encodedPoint)
            point.shouldBeInstanceOf<Point>()
            point.coordinates.longitude.degrees shouldBeEqual 100.0.degrees
            point.coordinates.latitude.degrees shouldBeEqual 0.0.degrees
            point.coordinates.altitude.shouldBeNone()
            point.bbox.shouldBeNone()
        }
    })
