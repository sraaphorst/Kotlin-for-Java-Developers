package mastermind

import kotlin.math.min

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    // Determine the number of letters in the correct position.
    val rightPosition = secret.zip(guess).count{ (s,g) -> s == g }

    // Determine the minimum letters shared in common.
    // See: http://mathworld.wolfram.com/Mastermind.html
    val commonLetters = ('A'..'F').sumBy { ch ->
        min(secret.count { s -> ch == s }, guess.count { g -> ch == g })
    }

    return Evaluation(rightPosition, commonLetters - rightPosition)
}
