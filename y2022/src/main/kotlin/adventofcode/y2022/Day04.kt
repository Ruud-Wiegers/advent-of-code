package adventofcode.y2022

import adventofcode.io.AdventSolution

object Day04 : AdventSolution(2022, 4, "Camp Cleanup") {

    override fun solvePartOne(input: String) = parse(input).count { (a, b) -> a in b || b in a }
    override fun solvePartTwo(input: String) = parse(input).count { (a, b) -> a overlaps b }

    private fun parse(input: String): List<Pair<IntRange, IntRange>> {
        val regex = Regex("""(\d+)-(\d+),(\d+)-(\d+)""")
        return input.lines()
            .map { regex.matchEntire(it)!!.destructured }
            .map { (a1, a2, b1, b2) ->
                a1.toInt()..a2.toInt() to b1.toInt()..b2.toInt()
            }
    }

    private operator fun IntRange.contains(o: IntRange) = o.first in this && o.last in this
    private infix fun IntRange.overlaps(o: IntRange) = last >= o.first && o.last >= first
}