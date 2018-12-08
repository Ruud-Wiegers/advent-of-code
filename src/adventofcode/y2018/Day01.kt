package adventofcode.y2018

import adventofcode.AdventSolution

object Day01 : AdventSolution(2018, 1, "Chronal Calibration") {

    override fun solvePartOne(input: String) =
            input.splitToSequence("\n")
                    .map(String::toInt)
                    .sum()

    override fun solvePartTwo(input: String): Int {
        val changes = input.split("\n").map(String::toInt)

        var frequency = 0
        val reached = mutableSetOf<Int>()

        generateSequence { changes }
                .flatten()
                .forEach {
                    reached += frequency
                    frequency += it
                    if (frequency in reached) return frequency
                }

        throw IllegalStateException()
    }
}
