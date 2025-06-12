package nl.robor.koord.units

import kotlin.math.PI

public enum class AngleUnit(internal val factor: Double, symbol: String) {
    RADIANS(factor = 1.0, "rad"),
    DEGREES(factor = PI / 180.0, "°");

    public fun convert(value: Double, sourceUnit: AngleUnit): Double {
        val inRadians = value * sourceUnit.factor
        return inRadians / this.factor
    }
}