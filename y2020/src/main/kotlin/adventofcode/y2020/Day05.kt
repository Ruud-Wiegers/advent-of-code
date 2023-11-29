package adventofcode.y2020

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() = Day05.solve()

object Day05 : AdventSolution(2020, 5, "Binary Boarding")
{
    override fun solvePartOne(input: String) = input
        .lineSequence()
        .maxOf(::parse)

    override fun solvePartTwo(input: String): Int
    {
        val seats = input
            .lineSequence()
            .map(::parse)
            .toSortedSet()

        return seats.first { it + 1 !in seats && it + 2 in seats } + 1
    }

    private fun parse(input: String) = input
        .map { if (it in "BR") 1 else 0 }
        .fold(0) { acc, c -> acc * 2 + c }
}
