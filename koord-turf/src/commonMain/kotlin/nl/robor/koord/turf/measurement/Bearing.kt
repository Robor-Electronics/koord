package nl.robor.koord.turf.measurement

import nl.robor.koord.geojson.Point
import nl.robor.koord.geojson.Position
import nl.robor.koord.units.Angle
import nl.robor.koord.units.Angle.Companion.degrees
import nl.robor.koord.units.Angle.Companion.radians
import nl.robor.koord.units.cos
import nl.robor.koord.units.sin
import kotlin.math.atan2

// Based on: https://github.com/Turfjs/turf/blob/master/packages/turf-bearing/index.ts
public fun Position.bearing(
    other: Position,
    final: Boolean = false
) : Angle {
    // Reverse calculation
    if (final) {
        return this.calculateFinalBearing(other)
    }

    val dLon = other.longitude.angle - longitude.angle
    val a = sin(dLon) * cos(other.latitude.angle)
    val b =
        cos(latitude.angle) * sin(other.latitude.angle) -
        sin(latitude.angle) * cos(other.latitude.angle) * cos(dLon)

    return atan2(a, b).radians
}

public fun Point.bearing(other: Point, final: Boolean = false) : Angle =
    this.coordinates.bearing(other.coordinates, final)

private fun Position.calculateFinalBearing(other: Position) : Angle {
    // Swap start and end
    return (other.bearing(this) + 180.degrees) % 360.degrees
}