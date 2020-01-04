package games.gameOfFifteen

import board.Cell
import board.Direction
import board.createGameBoard
import games.game.Game
import java.lang.IllegalStateException

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteenImpl(initializer)

class GameOfFifteenImpl(private val initializer: GameOfFifteenInitializer): Game {
    private val board = createGameBoard<Int>(4)

    override fun initialize() =
        board.getAllCells().zip(initializer.initialPermutation).forEach { board[it.first] = it.second }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean =
        (1..15).toList() ==  getNumbersInOrder().take(15)

    override fun processMove(direction: Direction) {
        val emptyCell = findEmptyCell()
        with (board) {
            emptyCell.getNeighbour(direction.reversed())?. let {
                board[emptyCell] = board[it]
                board[it] = null
            }
        }
    }

    override fun get(i: Int, j: Int): Int?
        = board.getCellOrNull(i, j)?.run(board::get)

    private fun getNumbersInOrder(): List<Int?> =
            board.getAllCells().map{ board[it] }

    private fun findEmptyCell(): Cell =
        board.find { it == null } ?: throw IllegalStateException("Board does not contain an empty square")
}