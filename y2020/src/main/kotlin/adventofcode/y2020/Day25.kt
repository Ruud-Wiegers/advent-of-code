package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day25.solve()

object Day25 : AdventSolution(2020, 25, "Combo Breaker D:<") {
    override fun solvePartOne(input: String): Long {
        val (k0, k1) = input.lines().map(String::toLong)
        return generateSequence(1L) { it * 7L % 20201227L }
            .takeWhile { it != k0 }
            .fold(1L) { a, _ -> a * k1 % 20201227L }
    }

    override fun solvePartTwo(input: String) = "Free Star!"
}
