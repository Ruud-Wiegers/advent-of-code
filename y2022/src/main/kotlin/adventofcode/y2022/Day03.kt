package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.solve

object Day03 : AdventSolution(2022, 3, "Rucksack Reorganization") {

    override fun solvePartOne(input: String) = input
        .lineSequence()
        .map { line ->
            line.chunked(line.length / 2, CharSequence::toSet)
                .reduce(Set<Char>::intersect)
        }
        .map(Set<Char>::single)
        .sumOf(::priority)

    override fun solvePartTwo(input: String) = input.lineSequence()
        .map(String::toSet)
        .chunked(3) { it.reduce(Set<Char>::intersect) }
        .map(Set<Char>::single)
        .sumOf(::priority)


    private fun priority(it: Char): Int =
        if (it in 'A'..'Z')
            priority(it.lowercaseChar()) + 26
        else
            it.code - 'a'.code + 1
}