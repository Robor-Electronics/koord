package nl.robor.koord.geojson

import dev.nesk.akkurate.annotations.Validate
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Validate
@JvmInline
@Serializable
public value class Degrees(
    public val value: Double,
) : Comparable<Degrees> {
    override fun compareTo(other: Degrees): Int = value.compareTo(other.value)
}

public val Double.degrees: Degrees
    get() = Degrees(this)
