package nl.robor.koord.turf.measurement

import nl.robor.koord.geojson.Point
import nl.robor.koord.geojson.Position
import nl.robor.koord.turf.toArcDistance
import nl.robor.koord.units.Angle
import nl.robor.koord.units.Angle.Companion.radians
import nl.robor.koord.units.Distance
import nl.robor.koord.units.cos
import nl.robor.koord.units.sin
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

// based on https://github.com/Turfjs/turf/blob/master/packages/turf-distance/index.ts
public fun Position.distance(to: Position): Distance {
    val dLat: Angle = to.latitude.angle - latitude.angle
    val dLon: Angle = to.longitude.angle - longitude.angle
    val a =
        sin(dLat / 2).pow(2) +
        sin(dLon / 2).pow(2) * cos(this.latitude.angle) * cos(to.latitude.angle)

    val result: Angle = (2 * atan2(sqrt(a), sqrt(1 - a))).radians
    return result.toArcDistance()
}

public fun Point.distance(to: Point) : Distance =
    this.coordinates.distance(to.coordinates)