package nl.robor.koord.turf.measurement

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import nl.robor.koord.geojson.Latitude
import nl.robor.koord.geojson.Longitude
import nl.robor.koord.geojson.Position
import nl.robor.koord.units.Angle.Companion.degrees

class BearingTest : FunSpec({
    test("bearing") {
        val start = Position(
            longitude = Longitude((-75).degrees).shouldBeRight(),
            latitude = Latitude(45.degrees).shouldBeRight()
        )
        val end = Position(
            longitude = Longitude(20.degrees).shouldBeRight(),
            latitude = Latitude(60.degrees).shouldBeRight()
        )

        val initialBearing = start.bearing(end)
        initialBearing.inDegrees shouldBe (37.75 plusOrMinus 0.01)

        val finalBearing = start.bearing(end, final = true)
        finalBearing.inDegrees shouldBe (120.01 plusOrMinus 0.01)
    }
})