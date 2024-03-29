package adventofcode.y2018

import adventofcode.io.AdventSolution
import adventofcode.util.collections.cycle


object Day01 : AdventSolution(2018, 1, "Chronal Calibration") {

    override fun solvePartOne(input: String) = input.lineSequence().sumOf(String::toInt)

    override fun solvePartTwo(input: String): Int? {
        val changes = input.lines().map(String::toInt)

        val reached = mutableSetOf<Int>()

        return changes.cycle()
                .scan(0, operation = Int::plus)
                .find { !reached.add(it) }
    }
}
