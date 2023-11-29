package adventofcode.y2015

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

object Day03 : AdventSolution(2015, 3, "Perfectly Spherical Houses in a Vacuum") {

    override fun solvePartOne(input: String) = input.visitEach().size

    override fun solvePartTwo(input: String): Int {
        val route1 = input.filterIndexed { i, _ -> i % 2 == 0 }.visitEach()
        val route2 = input.filterIndexed { i, _ -> i % 2 != 0 }.visitEach()
        val visited = route1 + route2

        return visited.size
    }

    private fun String.visitEach() = asSequence()
        .map { it.toDirection() }
        .map(Direction::vector)
        .scan(Vec2.origin, Vec2::plus)
        .toSet()

    private fun Char.toDirection() = when (this) {
        '>' -> Direction.RIGHT
        '<' -> Direction.LEFT
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        else -> throw IllegalArgumentException()
    }
}
