package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = repeat(10) { Day10.solve() }

object Day10 : AdventSolution(2020, 10, "Adapter Array")
{
    override fun solvePartOne(input: String) = input.lines()
        .map(String::toInt)
        .let { it + 0 }
        .sorted()
        .zipWithNext { a, b -> b - a }
        .groupingBy { it }
        .eachCount()
        .run { getValue(1) * (getValue(3) + 1) }

    override fun solvePartTwo(input: String): Long
    {
        val adapters = input.lines().map(String::toInt).sorted()

        val countPathsTo = mutableMapOf(0 to 1L)

        return adapters
            .associateWithTo(countPathsTo) { (1..3).map(it::minus).mapNotNull(countPathsTo::get).sum() }
            .getValue(adapters.last())
    }
}
