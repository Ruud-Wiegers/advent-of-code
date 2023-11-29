package adventofcode.y2016

import adventofcode.io.AdventSolution


object Day03 : AdventSolution(2016, 3, "Squares With Three Sides") {
    override fun solvePartOne(input: String) = parseInput(input)
        .chunked(3)
        .count { it.isValidTriagle() }

    override fun solvePartTwo(input: String) = parseInput(input)
        .withIndex()
        .groupBy({ it.index % 3 }, { it.value })
        .values
        .flatten()
        .chunked(3)
        .count { it.isValidTriagle() }

}

private fun parseInput(input: String) = input
    .splitToSequence("\n", " ")
    .filterNot(String::isBlank)
    .map(String::toInt)

private fun List<Int>.isValidTriagle() = sorted().let { (a, b, c) -> a + b > c }
