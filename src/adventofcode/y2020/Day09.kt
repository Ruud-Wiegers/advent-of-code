package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day09.solve()

object Day09 : AdventSolution(2020, 9, "Encoding Error")
{
    override fun solvePartOne(input: String): Long
    {
        val windowSize = 25
        val seq = input.lineSequence().map(String::toLong)

        val validSums = seq
            .windowed(windowSize) { it.drop(1).map { v -> v + it[0] } }
            .windowed(windowSize) { it.flatten() }

        return seq.drop(windowSize)
            .zip(validSums)
            .first { (n, valid) -> n !in valid }
            .first
    }

    override fun solvePartTwo(input: String) = input.lines()
        .map(String::toLong)
        .findSublistSummingTo(solvePartOne(input))
        .sorted()
        .let { it.first() + it.last() }

    private fun List<Long>.findSublistSummingTo(target: Long): List<Long>
    {
        var total = this[0]
        var low = 0
        var high = 0
        while (total != target)
        {
            if (total < target) total += this[++high]
            if (total > target) total -= this[low++]
        }

        return subList(low, high + 1)
    }
}