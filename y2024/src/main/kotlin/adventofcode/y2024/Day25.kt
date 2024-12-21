package adventofcode.y2024

import adventofcode.io.AdventSolution
import kotlin.math.abs

fun main() {
    Day25.solve()
}

object Day25 : AdventSolution(2024, 25, "???") {

    override fun solvePartOne(input: String): Int {
        val parsed = input.parseInput()

        return 0
    }

    override fun solvePartTwo(input: String) = "Free Star!"

    fun String.parseInput() = lines().map {
        it.split("   ").map(String::toInt)
    }
}