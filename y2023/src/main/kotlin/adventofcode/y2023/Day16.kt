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

         parsed = parse(input)

        return solve(parsed, Beam(Vec2(-1,0), Direction.RIGHT))

    }

    override fun solvePartTwo(input: String): Int {

         parsed = parse(input)

        val ys = input.lines().indices
        val xs = input.lines().first().indices

        val top = xs.map { Beam(Vec2(it, ys.first-1), Direction.DOWN) }
        val bottom = xs.map { Beam(Vec2(it, ys.last+1), Direction.UP) }
        val left = ys.map { Beam(Vec2(ys.first-1, it), Direction.RIGHT) }
        val right = ys.map { Beam(Vec2(xs.last+1, it), Direction.LEFT) }

        val initial = top + bottom + left + right

        return initial.maxOf { solve(parsed, it) }

    }

    private lateinit var parsed: Map<Vec2, Char>
    private val rays = mutableMapOf<Beam, Pair<List<Vec2>, List<Beam>>>()


    private fun trace(beam: Beam) = rays.getOrPut(beam) {
        val path = generateSequence(beam.position+beam.direction.vector) { it + beam.direction.vector }.takeWhile { parsed[it] == '.' }

        val next = (path.lastOrNull() ?: beam.position) + beam.direction.vector
         if (parsed.containsKey(next))  (path+next).toList() to listOf(beam.copy(position = next)) else path.toList() to emptyList()

    }


    private fun solve(parsed: Map<Vec2, Char>, initial: Beam): Int {
        val energized = mutableSetOf<Vec2>()
        val visited = mutableSetOf<Beam>()

        val (path, next) = trace(initial)
        energized += path
        val open = next.toMutableList()

        while (open.isNotEmpty()) {
            val c = open.removeLast()

            if (c in visited) continue

            visited += c
            val type = parsed[c.position]!!

            require(type !='.')
            bounce(type, c.direction).forEach {
                val exit = c.copy(direction = it)
                val (p2, n2) = trace(exit)
                energized += p2
                open += n2
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


private val memo= mutableMapOf<Pair<Char,Direction>,List<Direction>>()

private fun bounce(type: Char, incomingDirection: Direction): List<Direction> {
    return memo.getOrPut(type to incomingDirection) {
        when (type) {
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
}