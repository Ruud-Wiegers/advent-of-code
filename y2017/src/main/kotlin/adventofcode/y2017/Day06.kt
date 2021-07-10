package adventofcode.y2017

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.collections.takeWhileDistinct

fun main() = Day06.solve()

object Day06 : AdventSolution(2017, 6, "Memory Reallocation") {

    override fun solvePartOne(input: String) =
        generateSequence(parse(input), ::redistribute)
            .takeWhileDistinct()
            .count()

    override fun solvePartTwo(input: String): Int {
        val seen = generateSequence(parse(input), ::redistribute)
            .takeWhileDistinct()
            .last()

        return generateSequence(seen, ::redistribute).drop(1).indexOf(seen) + 1
    }

    private fun redistribute(state: List<Int>): List<Int> {
        val max = state.maxOrNull()!!
        val i = state.indexOf(max)
        val newState = state.map { it }.toMutableList()
        newState[i] = 0
        ((i + 1)..i + max).forEach {
            newState[it % newState.size]++
        }
        return newState
    }

    private fun parse(input: String) = input.split("\t").map(String::toInt)
}


