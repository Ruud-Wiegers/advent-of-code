package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.language.intcode.IntCodeProgram

fun main() = Day05.solve()

object Day05 : AdventSolution(2019, 5, "Sunny with a Chance of Asteroids") {

    override fun solvePartOne(input: String) = run(input, 1)

    override fun solvePartTwo(input: String) = run(input, 5)

    private fun run(data: String, moduleId: Long) = IntCodeProgram.fromData(data)
            .run {
                input(moduleId)
                execute()
                generateSequence(this::output).last()
            }

}
