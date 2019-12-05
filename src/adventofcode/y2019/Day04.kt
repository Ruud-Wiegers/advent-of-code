package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = repeat(5){Day04.solve()}

object Day04 : AdventSolution(2019, 4, "Secure Container") {

    override fun solvePartOne(input: String) = parseToRange(input).count { c ->
        val pairs = c.toString().zipWithNext()

        val increasing = pairs.all { (a, b) -> a <= b }
        val pair = pairs.any { (a, b) -> a == b }

        increasing && pair
    }

    override fun solvePartTwo(input: String) = parseToRange(input).count { c ->
        val increasing = c.toString().zipWithNext().all { (a, b) -> a <= b }
        val pair = c.toString().groupingBy { it }.eachCount().any { it.value == 2 }

        increasing && pair
    }

    private fun parseToRange(input: String): IntRange = input
            .split('-')
            .map(String::toInt)
            .let { (l, h) -> l..h }
}
