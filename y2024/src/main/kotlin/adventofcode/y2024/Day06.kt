package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.collections.firstDuplicate
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.xBounds
import adventofcode.util.vector.yBounds

fun main() {
    Day06.solve()
}

object Day06 : AdventSolution(2024, 6, "Guard Gallivant") {

    override fun solvePartOne(input: String): Int {
        val (start, obstacles) = parseInput(input)

        return path(start, obstacles).distinctBy { it.first }.count() - 1
    }

    override fun solvePartTwo(input: String): Int {
        val (start, obstacles) = parseInput(input)

        val candidates = path(start, obstacles).map { it.first }.toSet() - start

        return candidates.count { path(start, obstacles + it).firstDuplicate() != null }
    }
}

private fun path(start: Vec2, obstacles: Set<Vec2>): Sequence<Pair<Vec2, Direction>> {
    val xBounds = obstacles.xBounds()
    val yBounds = obstacles.yBounds()
    fun inBounds(v: Vec2) = v.x in xBounds && v.y in yBounds

    return generateSequence(start to Direction.UP) { (pos, dir) ->
        when {
            !inBounds(pos) -> null
            pos + dir.vector in obstacles -> pos to dir.turnRight
            else -> pos + dir.vector to dir
        }
    }
}


private fun parseInput(input: String): Pair<Vec2, Set<Vec2>> {
    val grid = input.lines()
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> c to Vec2(x, y) }
        }
        .groupBy(Pair<Char, Vec2>::first, Pair<Char, Vec2>::second)

    val start = grid['^'].orEmpty().single()
    val guards = grid['#'].orEmpty().toSet()

    return start to guards
}
