package nl.robor.koord.geojson

import arrow.core.Some
import arrow.core.getOrElse
import arrow.core.raise.option
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.accessors.kotlin.size
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.constrain
import dev.nesk.akkurate.validateWith
import kotlinx.serialization.Serializable
import nl.robor.koord.geojson.validation.accessors.coordinates
import nl.robor.koord.geojson.validation.accessors.northeast
import nl.robor.koord.geojson.validation.accessors.southwest
import kotlin.jvm.JvmInline

@Validate
@Serializable
@JvmInline
public value class BoundingBox private constructor(
    public val coordinates: DoubleArray,
) {
    private val hasAltitude: Boolean
        get() = coordinates.size == 6

    public val southwest: Position
        get() =
            if (hasAltitude) {
                Position(
                    longitude = Longitude.unchecked(coordinates[0]),
                    latitude = Latitude.unchecked(coordinates[1]),
                    altitude = Some(coordinates[2]),
                )
            } else {
                Position(
                    longitude = Longitude.unchecked(coordinates[0]),
                    latitude = Latitude.unchecked(coordinates[1]),
                )
            }

    public val northeast: Position
        get() =
            if (hasAltitude) {
                Position(
                    longitude = Longitude.unchecked(coordinates[3]),
                    latitude = Latitude.unchecked(coordinates[4]),
                    altitude = Some(coordinates[5]),
                )
            } else {
                Position(
                    longitude = Longitude.unchecked(coordinates[2]),
                    latitude = Latitude.unchecked(coordinates[3]),
                )
            }

    override fun toString(): String = "BoundingBox{$southwest, $northeast}"

    public companion object {
        public val validator: Validator.Runner<BoundingBox> =
            Validator<BoundingBox> {
                coordinates.size.constrain { size ->
                    size == 4 || size == 6
                }
                southwest.validateWith(Position.validator)
                northeast.validateWith(Position.validator)
            }

        public operator fun invoke(
            southwest: Position,
            northeast: Position,
        ): BoundingBox =
            option {
                val southWestAltitude = southwest.altitude.bind()
                val northeastAltitude = northeast.altitude.bind()
                BoundingBox(
                    coordinates =
                        doubleArrayOf(
                            southwest.longitude.degrees.value,
                            southwest.latitude.degrees.value,
                            southWestAltitude,
                            northeast.longitude.degrees.value,
                            northeast.latitude.degrees.value,
                            northeastAltitude,
                        ),
                )
            }.getOrElse {
                BoundingBox(
                    coordinates =
                        doubleArrayOf(
                            southwest.longitude.degrees.value,
                            southwest.latitude.degrees.value,
                            northeast.longitude.degrees.value,
                            northeast.latitude.degrees.value,
                        ),
                )
            }
    }
}
