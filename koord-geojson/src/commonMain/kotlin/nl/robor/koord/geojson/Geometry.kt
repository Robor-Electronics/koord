package nl.robor.koord.geojson

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

public val json: Json =
    Json {
        serializersModule =
            SerializersModule {
                polymorphic(Geometry::class) {
                    subclass(Point::class, Point.serializer())
                }
            }
        classDiscriminator = "type"
        prettyPrint = true
    }

@Serializable
public sealed interface Geometry : GeoJson
