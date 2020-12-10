package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day10.solve()

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
        val adapters = input.lines().map(String::toInt).toSortedSet()

        val counts = mutableMapOf(adapters.last() to 1L)

        fun countPaths(joltage: Int): Long = counts.getOrPut(joltage) {
            (1..3).map(joltage::plus).filter(adapters::contains).sumOf(::countPaths)
        }

        return countPaths(0)
    }
}
