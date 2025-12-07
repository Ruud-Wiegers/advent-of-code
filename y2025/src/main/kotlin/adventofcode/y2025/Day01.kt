package adventofcode.y2025

import adventofcode.io.AdventSolution
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() = Day01.solve()

object Day01 : AdventSolution(2025, 1, "Secret Entrance") {

    override fun solvePartOne(input: String) = parseInput(input)
        .solve()

    override fun solvePartTwo(input: String) = parseInput(input)
        .flatMap { generateSequence { it.sign }.take(it.absoluteValue) }
        .solve()

    private fun parseInput(input: String) = input
        .replace('L', '-')
        .replace('R', '+')
        .lineSequence()
        .map(String::toInt)

    private fun Sequence<Int>.solve(): Int = scan(50) { acc, i -> (acc + i % 100 + 100) % 100 }
        .count { it == 0 }
}