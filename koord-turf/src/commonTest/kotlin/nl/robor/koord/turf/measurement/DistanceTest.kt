package nl.robor.koord.turf.measurement

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import nl.robor.koord.geojson.Latitude
import nl.robor.koord.geojson.Longitude
import nl.robor.koord.geojson.Position
import nl.robor.koord.turf.toAngle
import nl.robor.koord.units.Angle.Companion.degrees
import kotlin.math.roundToInt

class DistanceTest : FunSpec({
    test("distance") {
        val p1 = Position(
            longitude = Longitude((-75.343).degrees).shouldBeRight(),
            latitude = Latitude(39.984.degrees).shouldBeRight()
        )
        val p2 = Position(
            longitude = Longitude((-75.534).degrees).shouldBeRight(),
            latitude = Latitude(39.123.degrees).shouldBeRight()
        )

        val distance = p1.distance(p2)
        distance.inKilometers shouldBe (97.12922118967835 plusOrMinus 1e-9)
        distance.inMillimeters shouldBe (97129221.1896783 plusOrMinus 1e-6)
        distance.toAngle().inRadians shouldBe (0.015245501024842149 plusOrMinus 1e-9)
        distance.toAngle().inDegrees shouldBe (0.8735028652858263 plusOrMinus 1e-9)
    }

    test("edge case") {
        val p1 = Position(
            longitude = Longitude((-180).degrees).shouldBeRight(),
            latitude = Latitude((-90).degrees).shouldBeRight()
        )
        val p2 = Position(
            longitude = Longitude(180.degrees).shouldBeRight(),
            latitude = Latitude((-90).degrees).shouldBeRight()
        )

        val distance = p1.distance(p2)
        distance.inMeters.roundToInt() shouldBeEqual 0
    }
})