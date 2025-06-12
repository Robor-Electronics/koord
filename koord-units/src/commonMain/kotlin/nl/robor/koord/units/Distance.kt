package nl.robor.koord.units

import kotlin.jvm.JvmInline
import kotlin.math.roundToLong
import kotlin.math.sign

@JvmInline
public value class Distance(private val meters: Double) : Comparable<Distance> {

    override fun compareTo(other: Distance): Int = this.meters.compareTo(other.meters)

    public companion object {
        public val ZERO: Distance = Distance(0.0)
        public val INFINITE: Distance = Distance(Double.POSITIVE_INFINITY)
        internal val NEG_INFINITE: Distance = Distance(Double.NEGATIVE_INFINITY)

        // Millimeters
        public inline val Int.millimeters: Distance get() = toDistance(DistanceUnit.MILLIMETERS)
        public inline val Int.mm: Distance get() = this.millimeters
        public inline val Long.millimeters: Distance get() = toDistance(DistanceUnit.MILLIMETERS)
        public inline val Long.mm: Distance get() = this.millimeters
        public inline val Double.millimeters: Distance get() = toDistance(DistanceUnit.MILLIMETERS)
        public inline val Double.mm: Distance get() = this.millimeters

        // Centimeters
        public inline val Int.centimeters: Distance get() = toDistance(DistanceUnit.CENTIMETERS)
        public inline val Int.cm: Distance get() = this.centimeters
        public inline val Long.centimeters: Distance get() = toDistance(DistanceUnit.CENTIMETERS)
        public inline val Long.cm: Distance get() = this.centimeters
        public inline val Double.centimeters: Distance get() = toDistance(DistanceUnit.CENTIMETERS)
        public inline val Double.cm: Distance get() = this.centimeters

        // Meters
        public inline val Int.meters: Distance get() = toDistance(DistanceUnit.METERS)
        public inline val Int.m: Distance get() = this.meters
        public inline val Long.meters: Distance get() = toDistance(DistanceUnit.METERS)
        public inline val Long.m: Distance get() = this.meters
        public inline val Double.meters: Distance get() = toDistance(DistanceUnit.METERS)
        public inline val Double.m: Distance get() = this.meters

        // Kilometers
        public inline val Int.kilometers: Distance get() = toDistance(DistanceUnit.KILOMETERS)
        public inline val Int.km: Distance get() = this.kilometers
        public inline val Long.kilometers: Distance get() = toDistance(DistanceUnit.KILOMETERS)
        public inline val Long.km: Distance get() = this.kilometers
        public inline val Double.kilometers: Distance get() = toDistance(DistanceUnit.KILOMETERS)
        public inline val Double.km: Distance get() = this.kilometers
    }

    public val inMillimeters: Double
        get() = this.meters / DistanceUnit.MILLIMETERS.factor

    public val inCentimeters: Double
        get() = this.meters / DistanceUnit.CENTIMETERS.factor

    public val inMeters: Double
        get() = this.meters

    public val inKilometers: Double
        get() = this.meters / DistanceUnit.KILOMETERS.factor

    public operator fun unaryMinus(): Distance = Distance(-meters)
    public operator fun plus(other: Distance): Distance {
        return Distance(this.meters + other.meters)
    }
    public operator fun minus(other: Distance) : Distance {
        return Distance(this.meters - other.meters)
    }

    public operator fun times(other: Double) : Distance {
        return Distance(this.meters * other)
    }

    public fun isInfinite(): Boolean = this == INFINITE || this == NEG_INFINITE
    public fun isFinite(): Boolean = !isInfinite()
}

public fun Int.toDistance(unit: DistanceUnit): Distance {
    return Distance(this * unit.factor)
}

public fun Long.toDistance(unit: DistanceUnit): Distance {
    return Distance(this * unit.factor)
}

public fun Double.toDistance(unit: DistanceUnit): Distance {
    return Distance(this * unit.factor)
}

