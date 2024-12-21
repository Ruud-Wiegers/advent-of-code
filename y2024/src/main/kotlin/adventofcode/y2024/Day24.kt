package adventofcode.y2024

import adventofcode.io.AdventSolution
import kotlin.math.abs

fun main() {
    Day24.solve()
}

object Day24 : AdventSolution(2024, 24, "???") {

    override fun solvePartOne(input: String): Int {
        val parsed = input.parseInput()

        return 0
    }

    override fun solvePartTwo(input: String): Int {
        val parsed = input.parseInput()

        return 0
    }

    fun String.parseInput() = lines().map {
        it.split("   ").map(String::toInt)
    }
}