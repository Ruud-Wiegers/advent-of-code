package adventofcode.y2021

import adventofcode.AdventSolution

object Day01 : AdventSolution(2021, 1, "Sonar Sweep")
{
    override fun solvePartOne(input: String) = input
        .lineSequence()
        .map(String::toInt)
        .zipWithNext { a, b -> a < b }
        .count { it }

    override fun solvePartTwo(input: String) = input
        .lineSequence()
        .map(String::toInt)
        .windowed(3) { it.sum() }
        .zipWithNext { a, b -> a < b }
        .count { it }
}
