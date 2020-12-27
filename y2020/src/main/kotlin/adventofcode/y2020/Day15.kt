package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day15.solve()

object Day15 : AdventSolution(2020, 15, "Rambunctious Recitation") {
    override fun solvePartOne(input: String) = input
        .split(',')
        .map(String::toInt)
        .let { recitation(it, 2020) }

    override fun solvePartTwo(input: String) = input
        .split(',')
        .map(String::toInt)
        .let { recitation(it, 30_000_000) }

    private fun recitation(initial: List<Int>, length: Int): Int {
        val seen = IntArray(length) { -1 }
        initial.dropLast(1).forEachIndexed { i, v -> seen[v] = i }

        var prev = initial.last()
        repeat(length - initial.size) {
            val b = it + initial.lastIndex
            val i = if (seen[prev] < 0) b else seen[prev]
            seen[prev] = b
            prev = b - i
        }
        return prev
    }
}
