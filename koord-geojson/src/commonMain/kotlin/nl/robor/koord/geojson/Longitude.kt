package nl.robor.koord.geojson

import arrow.core.Either
import arrow.core.NonEmptySet
import arrow.core.raise.either
import dev.nesk.akkurate.ValidationResult
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.arrow.bind
import dev.nesk.akkurate.constraints.ConstraintViolation
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

@Validate
@Serializable(with = Longitude.Serializer::class)
@JvmInline
public value class Longitude private constructor(
    public val angle: Angle,
) : Comparable<Longitude> {
    override fun compareTo(other: Longitude): Int = this.angle.compareTo(other.angle)

    public companion object {
        public val validator: Validator.Runner<Longitude> =
            Validator<Longitude> {
                angle.constrain { angle ->
                    (-(180.degrees)..180.degrees).contains(angle)
                }
            }

        public operator fun invoke(value: Angle): Either<NonEmptySet<ConstraintViolation>, Longitude> =
            either { bind(validator(Longitude(value))) }

        public fun unchecked(value: Angle): Longitude = Longitude(value)
    }

    public data object Serializer : KSerializer<Longitude> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Latitude", PrimitiveKind.DOUBLE)

        override fun serialize(
            encoder: Encoder,
            value: Longitude,
        ) {
            encoder.encodeDouble(value.angle.inDegrees)
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
