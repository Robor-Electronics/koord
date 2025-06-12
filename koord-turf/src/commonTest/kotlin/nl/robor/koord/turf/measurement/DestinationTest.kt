package nl.robor.koord.turf.measurement

import arrow.core.Some
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import nl.robor.koord.geojson.Latitude
import nl.robor.koord.geojson.Longitude
import nl.robor.koord.geojson.Position
import nl.robor.koord.units.Angle.Companion.degrees
import nl.robor.koord.units.Distance.Companion.kilometers
import nl.robor.koord.units.Distance.Companion.meters

class DestinationTest : FunSpec({
    test("bearing 0 with elevation") {
        val start = Position.invoke(
            longitude = Longitude((-75).degrees).shouldBeRight(),
            latitude = Latitude(38.10096062273525.degrees).shouldBeRight(),
            altitude = Some(100.meters)
        )

        val dest = start.destination(100.kilometers, 0.degrees)
        dest.validate().orThrow()
        dest.longitude.angle.inDegrees shouldBeEqual -75.0
        dest.latitude.angle.inDegrees shouldBe (39.000281 plusOrMinus 1e-7)
        dest.altitude.shouldBeSome().inMeters shouldBeEqual 100.0
    }

    test("bearing 0") {
        val start = Position.invoke(
            longitude = Longitude((-75).degrees).shouldBeRight(),
            latitude = Latitude(38.10096062273525.degrees).shouldBeRight(),
        )

        val dest = start.destination(100.kilometers, 0.degrees)
        dest.validate().orThrow()
        dest.longitude.angle.inDegrees shouldBeEqual -75.0
        dest.latitude.angle.inDegrees shouldBe (39.000281 plusOrMinus 1e-7)
        dest.altitude.shouldBeNone()
    }

    test("bearing 180") {
        val start = Position.invoke(
            longitude = Longitude((-75).degrees).shouldBeRight(),
            latitude = Latitude(39.degrees).shouldBeRight(),
        )

        val dest = start.destination(100.kilometers, 180.degrees)
        dest.validate().orThrow()
        dest.longitude.angle.inDegrees shouldBeEqual -75.0
        dest.latitude.angle.inDegrees shouldBe (38.10068 plusOrMinus 1e-6)
        dest.altitude.shouldBeNone()
    }

    test("bearing 90") {
        val start = Position.invoke(
            longitude = Longitude((-75).degrees).shouldBeRight(),
            latitude = Latitude(39.degrees).shouldBeRight(),
        )

        val dest = start.destination(100.kilometers, 90.degrees)
        dest.validate().orThrow()
        dest.longitude.angle.inDegrees shouldBe (-73.842853 plusOrMinus 1e-6)
        dest.latitude.angle.inDegrees shouldBe (38.994285 plusOrMinus 1e-6)
        dest.altitude.shouldBeNone()
    }

    test("far away") {
        val start = Position.invoke(
            longitude = Longitude((-75).degrees).shouldBeRight(),
            latitude = Latitude(39.degrees).shouldBeRight(),
        )

        val bearing = 90.degrees
        val distance = 5000.kilometers
        val dest = start.destination(distance, bearing)
        dest.validate().orThrow()
        dest.longitude.angle.inDegrees shouldBe (-22.885356 plusOrMinus 1e-6)
        dest.latitude.angle.inDegrees shouldBe (26.440011 plusOrMinus 1e-6)
        dest.altitude.shouldBeNone()
    }
})