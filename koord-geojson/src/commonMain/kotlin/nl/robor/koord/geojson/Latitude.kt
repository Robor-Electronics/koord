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

@Target(AnnotationTarget.FUNCTION)
public annotation class Unchecked

@Validate
@Serializable(with = Latitude.Serializer::class)
@JvmInline
public value class Latitude private constructor(
    public val degrees: Degrees,
) : Comparable<Latitude> {
    override fun compareTo(other: Latitude): Int = degrees.compareTo(other.degrees)

    public companion object {
        public val validator: Validator.Runner<Latitude> =
            Validator.Companion<Latitude> {
                degrees.value.isBetween(-90.0..90.0)
            }

        public operator fun invoke(value: Degrees): Either<NonEmptySet<ConstraintViolation>, Latitude> =
            either { bind(validator(Latitude(value))) }

        public operator fun invoke(value: Double): Either<NonEmptySet<ConstraintViolation>, Latitude> = invoke(value.degrees)

        internal fun unchecked(value: Degrees): Latitude = Latitude(value)

        internal fun unchecked(value: Double): Latitude = Latitude(value.degrees)
    }

    public data object Serializer : KSerializer<Latitude> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Latitude", PrimitiveKind.DOUBLE)

        override fun serialize(
            encoder: Encoder,
            value: Latitude,
        ) {
            encoder.encodeDouble(value.degrees.value)
        }

        override fun deserialize(decoder: Decoder): Latitude {
            val rawValue = decoder.decodeDouble()
            val validationResult = Latitude.validator(Latitude(Degrees(rawValue)))
            return when (validationResult) {
                is ValidationResult.Failure -> validationResult.orThrow()
                is ValidationResult.Success<Latitude> -> validationResult.value
            }
        }
    }
}
