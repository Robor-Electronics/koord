package nl.robor.koord.geojson

import arrow.core.raise.either
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BoundingBoxTest :
    FunSpec({
        val encodedBoundingBox =
            """
            [-10.0, -10.0, 10.0, 10.0]
            """.trimIndent()

        test("encode") {
            val boundingBox =
                either {
                    BoundingBox(
                        southwest =
                            Position(
                                longitude = Longitude(-10.0).bind(),
                                latitude = Latitude(-10.0).bind(),
                            ),
                        northeast =
                            Position(
                                longitude = Longitude(10.0).bind(),
                                latitude = Latitude(10.0).bind(),
                            ),
                    )
                }.shouldBeRight()

            val encoded = Json.encodeToString(boundingBox)
            encoded shouldEqualJson { encodedBoundingBox }
        }

        test("decode") {
            val bbox: BoundingBox = Json.decodeFromString(encodedBoundingBox)
            bbox.southwest.coordinates[0] shouldBeEqual -10.0
            bbox.southwest.coordinates[1] shouldBeEqual -10.0
            bbox.northeast.coordinates[0] shouldBeEqual 10.0
            bbox.northeast.coordinates[1] shouldBeEqual 10.0
        }
    })
