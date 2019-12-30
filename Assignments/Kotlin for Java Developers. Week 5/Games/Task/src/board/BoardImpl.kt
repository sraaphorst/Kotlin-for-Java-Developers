package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

open class SquareBoardImpl(override val width: Int): SquareBoard {
    val cells: List<Cell> by lazy {
        (0 until width * width).map {
            Cell(it / width + 1, it % width + 1)
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? =
            cells.firstOrNull { i == it.i && j == it.j }

    override fun getCell(i: Int, j: Int): Cell =
            cells.first { i == it.i && j == it.j }

    override fun getAllCells(): Collection<Cell> = cells

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
            jRange.mapNotNull { j -> getCellOrNull(i, j) }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
            iRange.mapNotNull { i -> getCellOrNull(i, j) }

    override fun Cell.getNeighbour(direction: Direction): Cell? =
            when (direction) {
                UP -> getCellOrNull(i-1, j)
                DOWN -> getCellOrNull(i+1, j)
                RIGHT -> getCellOrNull(i, j+1)
                LEFT -> getCellOrNull(i, j-1)
            }
}

class GameBoardImpl<T>(width: Int) : GameBoard<T>, SquareBoardImpl(width) {
    private val contents: MutableMap<Cell, T?> = getAllCells().map { it to null }.toMap().toMutableMap()

    override fun get(cell: Cell): T? =
            contents[cell]

    override fun set(cell: Cell, value: T?) {
        contents[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
            contents.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell? =
            contents.filterValues(predicate).keys.firstOrNull()


    override fun any(predicate: (T?) -> Boolean): Boolean =
            contents.filterValues(predicate).keys.any()

    override fun all(predicate: (T?) -> Boolean): Boolean =
            contents.filterValues(predicate).keys == contents.keys
}

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)


