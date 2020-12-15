package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day15.solve()

object Day15 : AdventSolution(2020, 15, "Rambunctious Recitation")
{
    override fun solvePartOne(input: String) = input.split(',')
        .map(String::toInt)
        .let(::recitation)
        .take(2020)
        .last()

    override fun solvePartTwo(input: String) = input.split(',')
        .map(String::toInt)
        .let(::recitation)
        .take(30_000_000)
        .last()

    private fun recitation(initial: List<Int>): Sequence<Int>
    {
        val seen = initial.dropLast(1).withIndex().associate { it.value to it.index }.toMutableMap()

        var index = initial.lastIndex
        var prev = initial.last()
        return sequence {
            yieldAll(initial.dropLast(1))

            while (true)
            {
                val i = seen[prev] ?: index
                yield(prev)
                seen[prev] = index++
                prev = index - i - 1
            }
        }
    }
}
