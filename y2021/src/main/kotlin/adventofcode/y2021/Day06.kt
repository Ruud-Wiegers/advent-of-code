package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main() {
    Day06.solve()
}

object Day06 : AdventSolution(2021, 6, "Lanternfish") {
    override fun solvePartOne(input: String) = solve(input, 80)
    override fun solvePartTwo(input: String) = solve(input, 256)

    private fun solve(input: String, days: Int): Long {
        val parsed: Map<Int, Int> = parseInput(input)
        val fish = List(9) { parsed[it]?.toLong() ?: 0L }
        return generateSequence(fish, ::next).elementAt(days).sum()
    }

    private fun next(fish: List<Long>) = List(9) {
        when (it) {
            8 -> fish[0]
            6 -> fish[0] + fish[it + 1]
            else -> fish[it + 1]
        }
    }

    private fun parseInput(input: String) =
        input.split(',')
            .map { it.toInt() }
            .groupingBy { it }
            .eachCount()

}
