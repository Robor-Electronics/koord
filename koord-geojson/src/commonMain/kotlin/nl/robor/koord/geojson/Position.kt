package nl.robor.koord.geojson

import arrow.core.None
import arrow.core.Option
import arrow.core.toOption
import dev.nesk.akkurate.ValidationResult
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.accessors.kotlin.size
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.builders.isBetween
import dev.nesk.akkurate.validateWith
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.DoubleArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import nl.robor.koord.geojson.validation.accessors.coordinates
import nl.robor.koord.geojson.validation.accessors.latitude
import nl.robor.koord.geojson.validation.accessors.longitude
import kotlin.jvm.JvmInline

@Validate
@Serializable(with = Position.Serializer::class)
@JvmInline
public value class Position private constructor(
    public val coordinates: DoubleArray,
) {
    public companion object {
        public val validator: Validator.Runner<Position> =
            Validator<Position> {
                coordinates.size.isBetween(2..3)
                longitude.validateWith(Longitude.validator)
                latitude.validateWith(Latitude.validator)
            }

        public operator fun invoke(
            longitude: Longitude,
            latitude: Latitude,
            altitude: Option<Double> = None,
        ): Position =
            altitude.fold(
                ifSome = { altitude ->
                    Position(doubleArrayOf(longitude.degrees.value, latitude.degrees.value, altitude))
                },
                ifEmpty = { Position(doubleArrayOf(longitude.degrees.value, latitude.degrees.value)) },
            )
    }

    public val longitude: Longitude
        get() = Longitude.unchecked(coordinates[0].degrees)

    public val latitude: Latitude
        get() = Latitude.unchecked(coordinates[1].degrees)

    public val altitude: Option<Double>
        get() = coordinates.getOrNull(2).toOption()

    public fun validate(): ValidationResult<Position> = validator(this)

    public data object Serializer : KSerializer<Position> {
        override val descriptor: SerialDescriptor =
            DoubleArraySerializer().descriptor

        override fun serialize(
            encoder: Encoder,
            value: Position,
        ) {
            encoder.encodeSerializableValue(DoubleArraySerializer(), value.coordinates)
        }

        override fun deserialize(decoder: Decoder): Position {
            val rawValue = decoder.decodeSerializableValue(DoubleArraySerializer())
            return when (val validationResult = Position(rawValue).validate()) {
                is ValidationResult.Failure -> validationResult.orThrow()
                is ValidationResult.Success<Position> -> validationResult.value
            }
        }
    }
}
