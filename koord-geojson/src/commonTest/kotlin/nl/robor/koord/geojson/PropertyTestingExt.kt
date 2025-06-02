package nl.robor.koord.geojson

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.map

val arbLongitude = Arb.double(-180.0..180.0).map { Longitude(it).shouldBeRight() }
val arbLatitude = Arb.double(-90.0, 90.0).map { Latitude(it).shouldBeRight() }
