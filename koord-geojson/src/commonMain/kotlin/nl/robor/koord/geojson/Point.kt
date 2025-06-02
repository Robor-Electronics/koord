@file:UseSerializers(
    OptionSerializer::class,
)

package nl.robor.koord.geojson

import arrow.core.None
import arrow.core.Option
import arrow.core.serialization.OptionSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
@SerialName("Point")
public data class Point(
    val coordinates: Position,
    override val bbox: Option<BoundingBox> = None,
) : Geometry
