package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Direction.*
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.plus
import adventofcode.y2024.Tile.*

fun main() = Day15.solve()

object Day15 : AdventSolution(2024, 15, "Warehouse Woes") {

    override fun solvePartOne(input: String): Int {
        val (grid, instructions) = parseInput(input)
        return solve(grid.toMutableMap(), instructions)
    }

    override fun solvePartTwo(input: String): Int {
        val (grid, instructions) = parseInput(input)
        return solve(widenInput(grid).toMutableMap(), instructions)
    }
}

private fun parseInput(input: String): Pair<Map<Vec2, Tile>, List<Direction>> {
    val (gridStr, stepsStr) = input.split("\n\n")

    val grid: Map<Vec2, Tile> = gridStr.lines()
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Vec2(x, y) to c }
        }
        .filter { (_, v) -> v in "O.@" }
        .associate { it }
        .mapValues {
            when (it.value) {
                '.' -> Floor
                '@' -> Player
                'O' -> Box
                else -> error("")
            }
        }

    val instructions = stepsStr.mapNotNull {
        when (it) {
            '^' -> UP
            '>' -> RIGHT
            'v' -> DOWN
            '<' -> LEFT
            else -> null
        }
    }

    return Pair(grid, instructions)
}

private fun widenInput(grid: Map<Vec2, Tile>): Map<Vec2, Tile> = grid.flatMap { (pos, type) ->
    listOf(
        pos.copy(x = pos.x * 2) to when (type) {
            Box -> LeftBox
            else -> type
        },
        pos.copy(x = pos.x * 2 + 1) to when (type) {
            Player -> Floor
            Box -> RightBox
            else -> type
        }
    )
}
    .associate { it }

private fun solve(grid: MutableMap<Vec2, Tile>, instructions: List<Direction>): Int {
    instructions.forEach { instruction ->
        val player = grid.filterValues { it == Player }.keys.single()
        if (grid.canMove(player, instruction)) {
            grid.move(player, instruction)
        }

    }
    return grid.filterValues { it == Box || it == LeftBox }.keys.sumOf { (x, y) -> x + 100 * y }
}


private fun Map<Vec2, Tile>.canMove(position: Vec2, dir: Direction): Boolean =
    when (this[position]) {
        Floor -> true
        Player, Box, LeftBox, RightBox -> canMove(position + dir, dir)
        null -> false
    } && when {
        dir in listOf(LEFT, RIGHT) -> true
        this[position] == LeftBox -> canMove(position + RIGHT + dir, dir)
        this[position] == RightBox -> canMove(position + LEFT + dir, dir)
        else -> true
    }

private fun MutableMap<Vec2, Tile>.move(position: Vec2, dir: Direction, moveSibling: Boolean = true) {
    when (this[position]) {
        Player, Box, LeftBox, RightBox -> move(position + dir, dir)
        Floor -> return
        null -> error("not allowed to move")
    }

    when {
        !moveSibling -> {}
        dir in listOf(LEFT, RIGHT) -> {}
        this[position] == LeftBox -> move(position + RIGHT, dir, moveSibling = false)
        this[position] == RightBox -> move(position + LEFT, dir, moveSibling = false)
    }

    this[position + dir] = this.getValue(position)
    this[position] = Floor
}


private enum class Tile { Floor, Box, LeftBox, RightBox, Player }
