package adventofcode.y2024

import adventofcode.io.AdventSolution
import kotlin.math.abs

fun main() {
    Day01.solve()
}

object Day01 : AdventSolution(2024, 1, "Historian Hysteria") {

    override fun solvePartOne(input: String): Int {
        val parsed = input.parseInput()
        val left = parsed.map { it.first() }.sorted()
        val right = parsed.map { it.last() }.sorted()

        return left.zip(right, Int::minus).sumOf(::abs)
    }

    override fun solvePartTwo(input: String): Int {
        val parsed = input.parseInput()
        val left = parsed.map { it.first() }
        val freq = parsed.map { it.last() }.groupingBy { it }.eachCount()

        return left.sumOf { it * (freq[it] ?: 0) }
    }

    fun String.parseInput() = lines().map {
        it.split("   ").map(String::toInt)
    }
}