package adventofcode.y2020

import adventofcode.io.AdventSolution
import adventofcode.util.collections.combinations

fun main() = Day09.solve()

object Day09 : AdventSolution(2020, 9, "Encoding Error")
{
    override fun solvePartOne(input: String): Long = input
        .lineSequence()
        .map(String::toLong)
        .windowed(26) { it.last() to it.take(25).combinations(Long::plus) }
        .first { (n, v) -> n !in v }
        .first

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
        while (high < this.lastIndex)
        {
            when
            {
                total < target -> total += this[++high]
                total > target -> total -= this[low++]
                else           -> return subList(low, high + 1)
            }
        }

        return emptyList()
    }
}