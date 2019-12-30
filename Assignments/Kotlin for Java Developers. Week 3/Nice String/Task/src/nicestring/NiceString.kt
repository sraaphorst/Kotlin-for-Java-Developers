package nicestring

fun String.isNice(): Boolean {
    val bubabe = setOf("ba", "be", "bu").none { this.contains(it) }
    val vowelCount = (count { it in "aeiou" } >= 3)
    val doubleLetter = zipWithNext().any{ it.first == it.second }
    return listOf(vowelCount, doubleLetter, bubabe).count{ it } >= 2
}
