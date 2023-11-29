package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.language.intcode.IntCodeProgram

fun main() = repeat(5) { Day09.solve() }

object Day09 : AdventSolution(2019, 9, "Sensor Boost") {

    override fun solvePartOne(input: String) = runProgram(input, 1)

    override fun solvePartTwo(input: String) = runProgram(input, 2)

    private fun runProgram(data: String, input: Long) = IntCodeProgram.fromData(data)
            .run {
                input(input)
                execute()
                generateSequence(this::output).toList()
            }
}
