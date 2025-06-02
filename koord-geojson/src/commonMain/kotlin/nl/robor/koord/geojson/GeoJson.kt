package nl.robor.koord.geojson

import arrow.core.Option
import kotlinx.serialization.Serializable

@Serializable
public sealed interface GeoJson {
    public val bbox: Option<BoundingBox>
}
