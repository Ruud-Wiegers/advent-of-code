package adventofcode.y2020

import adventofcode.io.AdventSolution

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
        val pathCounts = mutableMapOf(0 to 1L)
        fun countPathsFromPrevious(adapter: Int) = (1..3).map(adapter::minus).mapNotNull(pathCounts::get).sum()
        return adapters.associateWithTo(pathCounts, ::countPathsFromPrevious).getValue(adapters.last())
    }
}
