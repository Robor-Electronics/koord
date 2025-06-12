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
import dev.nesk.akkurate.constraints.constrain
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import nl.robor.koord.geojson.validation.accessors.angle
import nl.robor.koord.units.Angle
import nl.robor.koord.units.Angle.Companion.degrees
import kotlin.jvm.JvmInline

@Target(AnnotationTarget.FUNCTION)
public annotation class Unchecked

@Validate
@Serializable(with = Latitude.Serializer::class)
@JvmInline
public value class Latitude private constructor(
    public val angle: Angle,
) : Comparable<Latitude> {
    override fun compareTo(other: Latitude): Int = this.angle.compareTo(other.angle)

    public companion object {
        public val validator: Validator.Runner<Latitude> =
            Validator.Companion<Latitude> {
                angle.constrain { angle ->
                    (-(90.degrees)..90.degrees).contains(angle)
                }
            }

        public operator fun invoke(value: Angle): Either<NonEmptySet<ConstraintViolation>, Latitude> =
            either { bind(validator(Latitude(value))) }

        public fun unchecked(value: Angle): Latitude = Latitude(value)
    }

    public data object Serializer : KSerializer<Latitude> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Latitude", PrimitiveKind.DOUBLE)

        override fun serialize(
            encoder: Encoder,
            value: Latitude,
        ) {
            encoder.encodeDouble(value.angle.inDegrees)
        }

        override fun deserialize(decoder: Decoder): Latitude {
            val rawValue = decoder.decodeDouble()
            val validationResult = Latitude.validator(Latitude(rawValue.degrees))
            return when (validationResult) {
                is ValidationResult.Failure -> validationResult.orThrow()
                is ValidationResult.Success<Latitude> -> validationResult.value
            }
        }
    }
}
