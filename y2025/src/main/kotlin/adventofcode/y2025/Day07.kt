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
        val grid = input.lines()
        val start = Vec2(grid[0].indexOf('S'), 0)

        fun Vec2.firstSplitterHit() = generateSequence(this.y, Int::inc)
            .takeWhile { it <= grid.lastIndex }
            .find { y -> grid[y][this.x] == '^' }
            ?.let { y -> Vec2(this.x, y) }

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

