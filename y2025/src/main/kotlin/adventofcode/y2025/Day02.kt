package adventofcode.y2025

import adventofcode.io.AdventSolution


fun main() {
    Day02.solve()
}

object Day02 : AdventSolution(2025, 2, "Gift Shop") {

    override fun solvePartOne(input: String) = parse(input)
        .filter { it.length % 2 == 0 }
        .filter { it.isRepeating(it.length / 2) }
        .sumOf(String::toLong)

    override fun solvePartTwo(input: String) = parse(input)
        .filter { it.isRepeating() }
        .sumOf(String::toLong)

    private fun parse(input: String): Sequence<String> = input
        .splitToSequence(",")
        .map { it.split("-") }
        .map { it.map(String::toLong) }
        .flatMap { (a, b) -> a..b }
        .map { it.toString() }

    private fun String.isRepeating(): Boolean = (1..length / 2).any { isRepeating(it) }

    private fun String.isRepeating(sizeOfRepetition: Int): Boolean =
        this == this.take(sizeOfRepetition).repeat(length / sizeOfRepetition)
}