package games.gameOfFifteen

/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */
fun isEven(permutation: List<Int>): Boolean {
    val n = permutation.size

    // Generate all pairs (i < j), and check if P(i) > P(j).
    val calcs = (0 until n-1).flatMap { i -> (i+1 until n).map { j -> Pair(i, j) } }.count { (i, j) -> permutation[i] > permutation[j] }
    return calcs % 2 != 1

}