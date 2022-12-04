package adventofcode.y2022

import adventofcode.AdventSolution

object Day03 : AdventSolution(2022, 3, "Rucksack Reorganization") {

    override fun solvePartOne(input: String) = input
        .lineSequence()
        .map { line -> line.chunked(line.length / 2, CharSequence::toSet) }
        .map { it.reduce(Set<Char>::intersect).single() }
        .sumOf(::priority)

    override fun solvePartTwo(input: String) = input
        .lineSequence()
        .map(String::toSet)
        .chunked(3)
        .map { it.reduce(Set<Char>::intersect).single() }
        .sumOf(::priority)

    private fun priority(ch: Char): Int =
        if (ch in 'a'..'z')
            ch - 'a' + 1
        else
            ch - 'A' + 27
}