package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day03.solve()

object Day03 : AdventSolution(2020, 3, "Toboggan Trajectory")
{
    override fun solvePartOne(input: String) =
        countTreesOnSlope(input.lineSequence(), 3, 1)

    override fun solvePartTwo(input: String) =
        listOf(1 to 1,
               3 to 1,
               5 to 1,
               7 to 1,
               1 to 2)
            .map { (dx, dy) -> countTreesOnSlope(input.lineSequence(), dx, dy) }
            .map(Int::toLong)
            .reduce(Long::times)

    private fun countTreesOnSlope(lines: Sequence<String>, dx: Int, dy: Int) = lines
        .filterIndexed { i, _ -> i % dy == 0 }
        .mapIndexed { i, line -> line[i * dx % line.length] }
        .count { it == '#' }
}
