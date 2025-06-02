package nl.robor.koord.geojson

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Spec: [BoundingBox](https://datatracker.ietf.org/doc/html/rfc7946#section-5)
 */
@Serializable
@JvmInline
public value class BoundingBox private constructor(
    public val coordinates: DoubleArray,
) {
    public companion object {
    }
}