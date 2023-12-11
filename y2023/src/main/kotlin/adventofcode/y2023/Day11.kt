package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.collections.combinations
import adventofcode.util.vector.Vec2

fun main() {
    Day11.solve()
}

object Day11 : AdventSolution(2023, 11, "Cosmic Expansion") {

    override fun solvePartOne(input: String) = solve(input, expansion = 2)
    override fun solvePartTwo(input: String) = solve(input, expansion = 1_000_000)


    fun solve(input: String, expansion: Int): Long {
        val stars = parse(input)
        val expanded = expandSpace(stars, expansion)
        return expanded.combinations(Vec2::distance).sumOf(Int::toLong)
    }

    private fun parse(input: String): Set<Vec2> = input.lines()
        .flatMapIndexed { y, line ->
            line.withIndex()
                .filter { it.value == '#' }
                .map { (x) -> Vec2(x, y) }
        }
        .toSet()

    private fun expandSpace(stars: Set<Vec2>, expansion: Int): List<Vec2> {
        val nonEmptyColumns = stars.map { it.x }.toSet()
        val nonEmptyRows = stars.map { it.y }.toSet()

        val expandedX = (0..nonEmptyColumns.max()).scan(0) { x, col -> x + if (col in nonEmptyColumns) 1 else expansion }
        val expandedY = (0..nonEmptyRows.max()).scan(0) { y, row -> y + if (row in nonEmptyRows) 1 else expansion }

        return stars.map { Vec2(expandedX[it.x], expandedY[it.y]) }
    }
}
