package adventofcode.y2021

import adventofcode.io.AdventSolution
import kotlin.math.abs

object Day07 : AdventSolution(2021, 7, "The Treachery of Whales") {
    override fun solvePartOne(input: String): Int {
        val subs = parseInput(input).sorted()
        val median = subs[subs.size / 2]
        return subs.sumOf { abs(median - it) }
    }

    override fun solvePartTwo(input: String): Int {
        val parsed = parseInput(input)
        val min = parsed.minOrNull()!!
        val max = parsed.maxOrNull()!!
        return (min..max).minOf { c -> parsed.sumOf { t(abs(c - it)) } }
    }

    private fun t(i: Int) = i * (i + 1) / 2

    private fun parseInput(input: String): List<Int> {
        return input.split(',')
            .map { it.toInt() }
    }
}
