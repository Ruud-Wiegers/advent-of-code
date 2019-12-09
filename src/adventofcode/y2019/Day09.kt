package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntProgram9

fun main() = Day09.solve()

object Day09 : AdventSolution(2019, 9, "Sensor Boost") {

    override fun solvePartOne(input: String) = runProgram(input, 1)

    override fun solvePartTwo(input: String)= runProgram(input, 2)

    private fun runProgram(data: String, input: Long) = data
            .split(',')
            .map(String::toInt)
            .let(::IntProgram9)
            .apply {
                input(input)
                execute()
            }
            .output()
}
