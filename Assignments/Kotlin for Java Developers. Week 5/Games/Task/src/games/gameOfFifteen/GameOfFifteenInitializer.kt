package games.gameOfFifteen

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    /*
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
        val trivial: List<Int> = (1..15).toList()

        tailrec
        fun aux(permutation: List<Int>): List<Int> =
                if (isEven(permutation) && permutation != trivial)
                    permutation
                else
                    aux(permutation.shuffled())

        aux(trivial)
    }
}

