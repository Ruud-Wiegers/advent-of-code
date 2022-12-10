package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.solve

object Day10 : AdventSolution(2022, 10, "stuff") {

    override fun solvePartOne(input: String): Int {
        val signal = parseSignal(input)
        return (20..220 step 40).sumOf { it * signal[it - 1] }
    }

    override fun solvePartTwo(input: String) = parseSignal(input)
        .chunked(40)
        .joinToString("\n", prefix = "\n") {
            it.mapIndexed { index, signal -> if ((index - signal) in -1..1) '#' else ' ' }
                .joinToString("")
        }

    private fun parseSignal(input: String) = input.lines()
        .flatMap { if (it == "noop") listOf(0) else listOf(0) + it.substringAfter(" ").toInt() }
        .scan(1, Int::plus)
}

