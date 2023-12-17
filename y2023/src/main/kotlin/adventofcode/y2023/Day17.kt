package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import java.util.*

fun main() {
    Day17.solve()
}

object Day17 : AdventSolution(2023, 17, "Clumsy Crucible") {

    override fun solvePartOne(input: String): Int =
        solve(input, CartState::regularMove) { grid, cart -> cart.pos == grid.target }

    override fun solvePartTwo(input: String): Int =
        solve(input, CartState::ultraMove) { grid, cart -> cart.pos == grid.target && cart.movesInDirection > 3 }
}

private inline fun solve(
    input: String,
    neighbors: CartState.() -> List<CartState>,
    target: (City, CartState) -> Boolean
): Int {
    val grid = City(input)

    val visited = mutableSetOf<CartState>()

    val open = PriorityQueue(compareBy(Pair<CartState, Int>::second))
    open.add(CartState(grid.start, Direction.RIGHT, 0) to 0)
    while (open.isNotEmpty()) {
        val (oldState, oldCost) = open.remove()
        if (oldState in visited) continue
        visited += oldState
        if (target(grid, oldState)) return oldCost

        oldState.neighbors()
            .filter { it.pos in grid }
            .filter { it !in visited }
            .forEach { open.add(it to oldCost + grid.costAt(it.pos)) }


    }
    error("no valid path")
}

private data class City(val grid: List<List<Int>>) {

    constructor(input: String) : this(input.lines().map { it.map(Char::digitToInt) })

    private val xs = grid[0].indices
    private val ys = grid.indices

    operator fun contains(pos: Vec2): Boolean {
        return pos.x in xs && pos.y in ys
    }

    fun costAt(pos: Vec2) = grid[pos.y][pos.x]
    val start = Vec2.origin
    val target = Vec2(grid[0].lastIndex, grid.lastIndex)
}


private data class CartState(val pos: Vec2, val direction: Direction, val movesInDirection: Int) {
    private fun left() = CartState(pos + direction.turnLeft.vector, direction.turnLeft, 1)
    private fun right() = CartState(pos + direction.turnRight.vector, direction.turnRight, 1)


    private fun straight() = CartState(pos + direction.vector, direction, movesInDirection + 1)

    fun regularMove() = buildList {
        if (movesInDirection < 3) add(straight())
        add(left())
        add(right())
    }
    fun ultraMove() = buildList {
        if (movesInDirection < 10) add(straight())
        if (movesInDirection > 3) {
            add(left())
            add(right())
        }
    }
}
