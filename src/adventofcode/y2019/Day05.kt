package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram

fun main() = Day05.solve()

object Day05 : AdventSolution(2019, 5, "Sunny with a Chance of Asteroids") {

    override fun solvePartOne(input: String) = run(input, 1)

    override fun solvePartTwo(input: String) = run(input, 5)

    private fun run(input: String, moduleId: Long) = input
            .split(',')
            .map(String::toInt)
            .let(::IntCodeProgram)
            .run {
                input(moduleId)
                execute()
                generateSequence(this::output).last()
            }

}
