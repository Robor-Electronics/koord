package nl.robor.koord.geojson

import arrow.core.Either
import arrow.core.NonEmptySet
import arrow.core.raise.either
import dev.nesk.akkurate.ValidationResult
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.arrow.bind
import dev.nesk.akkurate.constraints.ConstraintViolation
import dev.nesk.akkurate.constraints.builders.isBetween
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import nl.robor.koord.geojson.validation.accessors.degrees
import nl.robor.koord.geojson.validation.accessors.value
import kotlin.jvm.JvmInline

@Validate
@Serializable(with = Longitude.Serializer::class)
@JvmInline
public value class Longitude private constructor(
    public val degrees: Degrees,
) : Comparable<Longitude> {
    override fun compareTo(other: Longitude): Int = degrees.compareTo(other.degrees)

    public companion object {
        public val validator: Validator.Runner<Longitude> =
            Validator<Longitude> {
                degrees.value.isBetween(-180.0..180.0)
            }

        public operator fun invoke(value: Degrees): Either<NonEmptySet<ConstraintViolation>, Longitude> =
            either { bind(validator(Longitude(value))) }

        public operator fun invoke(value: Double): Either<NonEmptySet<ConstraintViolation>, Longitude> = invoke(value.degrees)

        internal fun unchecked(value: Degrees): Longitude = Longitude(value)

        internal fun unchecked(value: Double): Longitude = Longitude(value.degrees)
    }

    public data object Serializer : KSerializer<Longitude> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Latitude", PrimitiveKind.DOUBLE)

        override fun serialize(
            encoder: Encoder,
            value: Longitude,
        ) {
            encoder.encodeDouble(value.degrees.value)
        }

        override fun deserialize(decoder: Decoder): Longitude {
            val rawValue = decoder.decodeDouble()
            val validationResult = validator(Longitude(rawValue.degrees))
            return when (validationResult) {
                is ValidationResult.Failure -> validationResult.orThrow()
                is ValidationResult.Success<Longitude> -> validationResult.value
            }
        }
    }
}
