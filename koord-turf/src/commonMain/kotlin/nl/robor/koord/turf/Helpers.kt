package nl.robor.koord.turf

import nl.robor.koord.units.Angle
import nl.robor.koord.units.Angle.Companion.radians
import nl.robor.koord.units.Distance
import nl.robor.koord.units.Distance.Companion.meters

public val EARTH_RADIUS: Distance = 6371008.8.meters

public fun Angle.toArcDistance(radius: Distance = EARTH_RADIUS) : Distance {
    return radius * this.inRadians
}

public fun Distance.toAngle(radius: Distance = EARTH_RADIUS) : Angle {
    return (this.inMeters / radius.inMeters).radians
}
