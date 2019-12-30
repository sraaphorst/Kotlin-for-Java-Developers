package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger

class Rational(n: BigInteger, d: BigInteger): Comparable<Rational> {
    val numerator: BigInteger
    val denominator: BigInteger
    init {
        require(d != BigInteger.ZERO) {"Denominator cannot be zero"}
        val gcd = n.gcd(d)
        numerator = d.signum().toBigInteger() * n / gcd
        denominator = d.signum().toBigInteger() * d / gcd
    }

    operator fun plus(other: Rational): Rational =
            Rational(numerator * other.denominator + denominator * other.numerator, denominator * other.denominator)

    operator fun minus(other: Rational): Rational =
            Rational(numerator * other.denominator - denominator * other.numerator, denominator * other.denominator)

    operator fun times(other: Rational): Rational =
            Rational(numerator * other.numerator, denominator * other.denominator)

    operator fun div(other: Rational): Rational =
            Rational(numerator * other.denominator, denominator * other.numerator)

    operator fun unaryMinus(): Rational =
            Rational(-numerator, denominator)

    override fun compareTo(other: Rational): Int =
        (numerator * other.denominator - other.numerator * denominator).signum()

    override fun toString(): String {
        if (numerator == BigInteger.ZERO) return "0"
        return numerator.toString() + (if (denominator != BigInteger.ONE) "/$denominator" else "")
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rational

        if (numerator != other.numerator) return false
        if (denominator != other.denominator) return false

        return true
    }
}

infix fun BigInteger.divBy(other: BigInteger): Rational =
        Rational(this, other)

infix fun Int.divBy(other: Int): Rational =
        Rational(this.toBigInteger(), other.toBigInteger())

infix fun Long.divBy(other: Long): Rational =
        Rational(this.toBigInteger(), other.toBigInteger())

fun String.toRational(): Rational {
    fun fail(): Nothing = throw IllegalArgumentException("Expecting rational in form of n/d or n, received $this")
    if ('/' !in this) {
        val number = toBigIntegerOrNull() ?: fail()
        return Rational(number, BigInteger.ONE)
    }
    val (numerText, denomText) = this.split('/')
    val numer = numerText.toBigIntegerOrNull() ?: fail()
    val denom = denomText.toBigIntegerOrNull() ?: fail()
    return Rational(numer, denom)
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}