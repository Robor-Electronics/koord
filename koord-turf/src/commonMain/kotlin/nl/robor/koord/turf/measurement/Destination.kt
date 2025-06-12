package nl.robor.koord.turf.measurement

import nl.robor.koord.geojson.Latitude
import nl.robor.koord.geojson.Longitude
import nl.robor.koord.geojson.Position
import nl.robor.koord.turf.toAngle
import nl.robor.koord.units.Angle
import nl.robor.koord.units.Angle.Companion.radians
import nl.robor.koord.units.Distance
import nl.robor.koord.units.cos
import nl.robor.koord.units.sin
import kotlin.math.asin
import kotlin.math.atan2

public fun Position.destination(distance: Distance, bearing: Angle): Position {
    val distanceRadians = distance.toAngle()
    val lat2 = asin(
        sin(this.latitude.angle) * cos(distanceRadians) +
                cos(this.latitude.angle) * sin(distanceRadians) * cos(bearing)
    ).radians
    val lon2 = atan2(
        sin(bearing) * sin(distanceRadians) * cos(this.latitude.angle),
        cos(distanceRadians) - sin(this.latitude.angle) * sin(lat2)
    ).radians + this.longitude.angle
    return Position.invoke(
        longitude = Longitude.unchecked(lon2),
        latitude = Latitude.unchecked(lat2),
        altitude = altitude
    )
}
