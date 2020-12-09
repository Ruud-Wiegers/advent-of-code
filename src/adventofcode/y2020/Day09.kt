package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day09.solve()

object Day09 : AdventSolution(2020, 9, "Encoding Error")
{
    override fun solvePartOne(input: String): Long
    {
        val seq = input.lineSequence().map(String::toLong)
        return seq
            .windowed(25, partialWindows = false) { it.drop(1).map { r -> r + it[0] } }
            .windowed(25, partialWindows = false) { it.flatten().toSet() }
            .zip(seq.drop(25))
            .first { (valid, n) -> n !in valid }
            .second
    }

    override fun solvePartTwo(input: String): Long
    {
        val target = solvePartOne(input)
        val seq = input.lines().map(String::toLong)

        var total = seq[0]
        var low = 0
        var high = 0
        while (total != target)
        {
            if (total < target) total += seq[++high]
            if (total > target) total -= seq[low++]
        }

        return seq.subList(low, high + 1).sorted().let { it.first() + it.last() }
    }
}
