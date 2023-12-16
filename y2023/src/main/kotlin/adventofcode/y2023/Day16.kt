package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

fun main() {
    Day16.solve()
}

object Day16 : AdventSolution(2023, 16, "The Floor Will Be Lava") {

    override fun solvePartOne(input: String): Int {

        val parsed = parse(input)

        return solve(parsed, Beam(Vec2.origin, Direction.RIGHT))

    }

    override fun solvePartTwo(input: String): Int {

        val parsed = parse(input)

        val ys = input.lines().indices
        val xs = input.lines().first().indices

        val top = xs.map { Beam(Vec2(it, ys.first), Direction.DOWN) }
        val bottom = xs.map { Beam(Vec2(it, ys.last), Direction.UP) }
        val left = ys.map { Beam(Vec2(xs.first, it), Direction.RIGHT) }
        val right = ys.map { Beam(Vec2(xs.last, it), Direction.LEFT) }

        val initial = top + bottom + left + right

        return initial.maxOf { solve(parsed, it) }

    }


    private fun solve(parsed: Map<Vec2, Char>, initial: Beam): Int {
        val visited = mutableSetOf<Beam>()

        val open = mutableListOf(initial)

        while (open.isNotEmpty()) {
            val c = open.removeLast()

            if (c in visited) continue

            val type = parsed[c.position] ?: continue

            open += bounce(type, c.direction).map { Beam(c.position + it.vector, it) }
            visited += c

        }

        return visited.map { it.position }.toSet().size
    }

}

private data class Beam(val position: Vec2, val direction: Direction)

private fun parse(input: String): Map<Vec2, Char> = input.lines()
    .flatMapIndexed { y, line ->
        line.withIndex()
            .map { (x, value) -> Vec2(x, y) to value }
    }
    .toMap()

private fun bounce(type: Char, incomingDirection: Direction): List<Direction> {
    return when (type) {
        '\\' -> when (incomingDirection) {
            Direction.UP, Direction.DOWN -> incomingDirection.turnLeft
            else -> incomingDirection.turnRight
        }.let(::listOf)
        '/' -> when (incomingDirection) {
            Direction.UP, Direction.DOWN -> incomingDirection.turnRight
            else -> incomingDirection.turnLeft
        }.let(::listOf)

        '-' -> listOf(Direction.LEFT, Direction.RIGHT) - incomingDirection.reverse
        '|' -> listOf(Direction.UP, Direction.DOWN) - incomingDirection.reverse


        else -> listOf(incomingDirection)
    }
}