package nl.robor.koord.geojson

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.map
import nl.robor.koord.units.Angle.Companion.degrees

val arbLongitude = Arb.double(-180.0..180.0).map { Longitude(it.degrees).shouldBeRight() }
val arbLatitude = Arb.double(-90.0, 90.0).map { Latitude(it.degrees).shouldBeRight() }
