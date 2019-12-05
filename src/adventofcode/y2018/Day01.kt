package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.util.scan


object Day01 : AdventSolution(2018, 1, "Chronal Calibration") {

    override fun solvePartOne(input: String) = input.lineSequence().sumBy(String::toInt)

    override fun solvePartTwo(input: String): Int? {
        val changes = input.lines().map(String::toInt)

        val reached = mutableSetOf<Int>()

        return generateSequence { changes }
                .flatten()
                .scan(0, operation = Int::plus)
                .find { !reached.add(it) }
    }
}
