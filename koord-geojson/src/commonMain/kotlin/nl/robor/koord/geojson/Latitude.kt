package nl.robor.koord.geojson

import arrow.core.Either
import arrow.core.NonEmptySet
import arrow.core.raise.either
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.arrow.bind
import dev.nesk.akkurate.constraints.ConstraintViolation
import dev.nesk.akkurate.constraints.builders.isBetween
import kotlinx.serialization.Serializable
import nl.robor.koord.geojson.validation.accessors.value
import kotlin.jvm.JvmInline

@Validate
@Serializable
@JvmInline
public value class Latitude private constructor(
    public val value: Double,
) {
    public companion object {
        public val validator: Validator.Runner<Latitude> =
            Validator.Companion<Latitude> {
                value.isBetween(-90.0..90.0)
            }

        public operator fun invoke(value: Double): Either<NonEmptySet<ConstraintViolation>, Latitude> =
            either { bind(validator(Latitude(value))) }
    }
}