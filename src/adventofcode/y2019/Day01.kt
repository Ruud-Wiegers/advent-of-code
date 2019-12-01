package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day01.solve()

object Day01 : AdventSolution(2019, 1, "The Tyranny of the Rocket Equation") {

    override fun solvePartOne(input: String) = input
            .lineSequence()
            .map(String::toInt)
            .sumBy(this::fuel)

    override fun solvePartTwo(input: String) = input
            .lineSequence()
            .map(String::toInt)
            .sumBy(this::tyrannousFuel)

    private fun fuel(mass: Int) = mass / 3 - 2

    private fun tyrannousFuel(mass: Int) = generateSequence(mass, this::fuel)
            .drop(1)
            .takeWhile { it > 0 }
            .sum()
}
