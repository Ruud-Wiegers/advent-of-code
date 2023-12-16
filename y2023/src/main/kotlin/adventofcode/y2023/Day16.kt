package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Direction.*
import adventofcode.util.vector.Vec2

fun main() {
    Day16.solve()
}

object Day16 : AdventSolution(2023, 16, "The Floor Will Be Lava") {

    override fun solvePartOne(input: String): Int {
        val parsed = parse(input)

        return solve(parsed, Beam(Vec2(-1, 0), RIGHT))

    }

    override fun solvePartTwo(input: String): Int {

        val parsed = parse(input)

        val ys = input.lines().indices
        val xs = input.lines().first().indices

        val top = xs.map { Beam(Vec2(it, ys.first - 1), DOWN) }
        val bottom = xs.map { Beam(Vec2(it, ys.last + 1), UP) }
        val left = ys.map { Beam(Vec2(ys.first - 1, it), RIGHT) }
        val right = ys.map { Beam(Vec2(xs.last + 1, it), LEFT) }

        val initial = top + bottom + left + right

        return initial.maxOf { solve(parsed, it) }

    }

    private val rays = mutableMapOf<Beam, Pair<List<Vec2>, Beam?>>()

    private fun trace(parsed: Map<Vec2, Char>, beam: Beam) = rays.getOrPut(beam) {
        var next = beam.position
        val path = mutableListOf<Vec2>()
        do {
            next += beam.direction.vector
            path += next
        } while (parsed[next] == '.')

        if (!parsed.containsKey(next)) {
            path.removeAt(path.lastIndex)
            path to null
        } else
            path to beam.copy(position = next)

    }


    private fun solve(parsed: Map<Vec2, Char>, initial: Beam): Int {
        val energized = mutableSetOf<Vec2>()
        val visited = mutableSetOf<Beam>()

        val (path, next) = trace(parsed, initial)
        energized += path
        val open = listOfNotNull(next).toMutableList()

        while (open.isNotEmpty()) {
            val c = open.removeLast()

            if (c in visited) continue

            visited += c
            val type = parsed[c.position]!!

            require(type !='.')
            bounces.getValue(type).getValue(c.direction).forEach {
                val exit = c.copy(direction = it)
                val (p2, n2) = trace(parsed, exit)
                energized += p2
                n2?.let(open::add)
            }
            energized += c.position
        }

        return energized.size
    }


}

private data class Beam(val position: Vec2, val direction: Direction)

private fun parse(input: String): Map<Vec2, Char> = input.lines()
    .flatMapIndexed { y, line ->
        line.withIndex()
            .map { (x, value) -> Vec2(x, y) to value }
    }
    .toMap()


private val bounces: Map<Char, Map<Direction, List<Direction>>> = mapOf(
    '\\' to mapOf(
        UP to LEFT,
        RIGHT to DOWN,
        DOWN to RIGHT,
        LEFT to UP,
    ).mapValues { listOf(it.value) },

    '/' to mapOf(
        UP to RIGHT,
        RIGHT to UP,
        DOWN to LEFT,
        LEFT to DOWN
    ).mapValues { listOf(it.value) },

    '-' to mapOf(
        UP to listOf(LEFT, RIGHT),
        RIGHT to listOf(RIGHT),
        DOWN to listOf(LEFT, RIGHT),
        LEFT to listOf(LEFT)
    ),

    '|' to mapOf(
        UP to listOf(UP),
        RIGHT to listOf(UP, DOWN),
        DOWN to listOf(DOWN),
        LEFT to listOf(UP, DOWN)
    )
)
