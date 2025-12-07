package adventofcode.y2025

import adventofcode.io.AdventSolution
import adventofcode.util.vector.plus
import adventofcode.util.vector.Direction.*
import adventofcode.util.vector.Vec2

fun main() {
    Day07.solve()
}

object Day07 : AdventSolution(2025, 7, "Laboratories") {

    override fun solvePartOne(input: String): Any {
        val grid = input.lines()
        val start = grid[0].indexOf('S')

        var splitCount = 0

        var beams = setOf(start)
        for (y in 1..grid.lastIndex) {
            beams = buildSet {
                for (x in beams) {
                    if (grid[y][x] == '^') {
                        splitCount++
                        add(x - 1)
                        add(x + 1)
                    } else {
                        add(x)
                    }
                }
            }
        }
        return splitCount
    }

    override fun solvePartTwo(input: String): Long {
        val (start, splitters) = parse(input)
        val bottom = splitters.maxOf { it.y }

        fun Vec2.fall() = generateSequence(this) { it + DOWN }.takeWhile { it.y <= bottom }
        fun Vec2.firstSplitterHit() = fall().find { it in splitters }

        val cache = mutableMapOf<Vec2, Long>()
        fun countSplits(position: Vec2): Long = cache.getOrPut(position) {
            val splitter = position.firstSplitterHit()
            if (splitter == null) 1 else {
                val left = countSplits(splitter + LEFT + DOWN)
                val right = countSplits(splitter + RIGHT + DOWN)
                left + right
            }
        }

        return countSplits(start)
    }
}

private fun parse(input: String): Pair<Vec2, Set<Vec2>> {

    lateinit var start: Vec2
    val splitters = buildSet {
        input.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                if (ch == 'S') start = Vec2(x, y)
                else if (ch == '^') add(Vec2(x, y))
            }
        }
    }
    return Pair(start, splitters)
}

