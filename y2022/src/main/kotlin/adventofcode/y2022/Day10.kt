package adventofcode.y2022

import adventofcode.io.AdventSolution


object Day10 : AdventSolution(2022, 10, "Cathode-Ray Tube") {

    override fun solvePartOne(input: String): Int {
        val signal = parseSignal(input)
        return (20..220 step 40).sumOf { it * signal[it - 1] }
    }

    override fun solvePartTwo(input: String) = parseSignal(input)
        .chunked(40)
        .joinToString("\n", prefix = "\n") { row ->
            row.mapIndexed { index, signal -> (index - signal) in -1..1 }
                .joinToString("") { if (it) "\u2588" else " " }
        }

    private fun parseSignal(input: String) = input.lines()
        .flatMap { if (it == "noop") listOf(0) else listOf(0) + it.substringAfter(" ").toInt() }
        .scan(1, Int::plus)
}

