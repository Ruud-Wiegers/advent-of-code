package adventofcode.y2025

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

fun main() {
    Day07.solve()
}

object Day07 : AdventSolution(2025, 7, "???") {

    override fun solvePartOne(input: String): Any {

        val (start, splitters) = parse(input)

        val bottom = splitters.maxOf { it.y }

        fun Vec2.fall() = generateSequence(this) { it + Direction.DOWN.vector }.takeWhile { it.y <= bottom }

        fun Vec2.firstSplitterHit() = fall().find { it in splitters }

        val cache = mutableMapOf<Vec2,Int>()
        fun countSplits(position: Vec2): Int = cache.getOrPut(position) {
            val splitter = position.firstSplitterHit() ?: return 0
            println(splitter)
            val left = countSplits(splitter + Direction.LEFT.vector + Direction.DOWN.vector)
            val right = countSplits(splitter + Direction.RIGHT.vector+ Direction.DOWN.vector)
            return left + 1 + right
        }

        return countSplits(start)
    }

    override fun solvePartTwo(input: String): Int {
        return TODO()
    }
}

private data class Tachyons(val start: Vec2, val splitters: Set<Vec2>)

private fun parse(input: String): Tachyons {

    lateinit var start: Vec2
    val splitters = buildSet {
        input.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                if (ch == 'S') start = Vec2(x, y)
                else if (ch == '^') add(Vec2(x, y))
            }
        }
    }
    return Tachyons(start, splitters)
}

