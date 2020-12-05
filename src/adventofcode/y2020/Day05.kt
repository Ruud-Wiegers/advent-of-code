package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day05.solve()

object Day05 : AdventSolution(2020, 5, "Binary Boarding")
{
    override fun solvePartOne(input: String) = input
        .lineSequence()
        .map(::parse)
        .maxOrNull()

    override fun solvePartTwo(input: String) = input
        .lineSequence()
        .map(::parse)
        .sorted()
        .zipWithNext()
        .first { (a, b) -> b - a == 2 }
        .let { it.first + 1 }

    private fun parse(input: String) = input
        .map { if (it in "BR") 1 else 0 }
        .fold(0) { acc, c -> acc * 2 + c }
}
