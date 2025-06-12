import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeExactly
import nl.robor.koord.units.Distance.Companion.mm

class DistanceTest : StringSpec({
    "addition should throw on overflow" {
        val a = Long.MAX_VALUE.mm
        val b = 1.mm
        shouldThrow<ArithmeticException> {
            a + b
        }
    }

    "adding Long.MAX_VALUE and Long.MIN_VALUE results in -1" {
        val a = Long.MAX_VALUE.mm
        val b = Long.MIN_VALUE.mm
        val result = a + b

        result.inMillimeters shouldBeExactly -1L
    }

    "substraction should throw on overflow" {
        val a = Long.MIN_VALUE.mm
        val b = 1L.mm
        shouldThrow<ArithmeticException> {
            val result = a - b // 💥 Throws ArithmeticException
        }
    }
})