package adventofcode.y2021

import adventofcode.io.AdventSolution

object Day01 : AdventSolution(2021, 1, "Sonar Sweep")
{
    override fun solvePartOne(input: String) = solve(input, 2)

    override fun solvePartTwo(input: String) = solve(input, 4)

    private fun solve(input: String, sweep: Int) = input
        .lineSequence()
        .map(String::toInt)
        .windowed(sweep)
        .count { it.first() < it.last() }
}
