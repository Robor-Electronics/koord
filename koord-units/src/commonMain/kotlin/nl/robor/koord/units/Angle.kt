package nl.robor.koord.units

import kotlin.jvm.JvmInline
import kotlin.math.cos
import kotlin.math.sin

@JvmInline
public value class Angle(private val radians: Double) : Comparable<Angle> {
    override fun compareTo(other: Angle): Int = this.radians.compareTo(other.radians)

    public operator fun unaryMinus(): Angle = Angle(-radians)
    public operator fun plus(other: Angle): Angle = Angle(this.radians + other.radians)

    public operator fun minus(other: Angle): Angle = Angle(this.radians - other.radians)

    public operator fun times(scalar: Double): Angle = Angle(this.radians * scalar)

    public operator fun div(scalar: Double): Angle = Angle(this.radians / scalar)

    public operator fun div(scalar: Int): Angle = Angle(this.radians / scalar)

    public operator fun rem(other: Angle): Angle {
        return radians.rem(other.radians).radians
    }

    public val inRadians: Double
        get() = radians

    public val inDegrees: Double
        get() = AngleUnit.DEGREES.convert(radians, AngleUnit.RADIANS)

    public companion object {
        public val Double.degrees: Angle
            get() = Angle(this * AngleUnit.DEGREES.factor)
        public val Int.degrees: Angle
            get() = this.toDouble().degrees
        public val Long.degrees: Angle
            get() = this.toDouble().degrees

        public val Double.radians: Angle
            get() = Angle(this)
        public val Int.radians: Angle
            get() = this.toDouble().radians
        public val Long.radians: Angle
            get() = this.toDouble().radians
    }
}

public fun sin(angle: Angle): Double = sin(angle.inRadians)

public fun cos(angle: Angle): Double = cos(angle.inRadians)