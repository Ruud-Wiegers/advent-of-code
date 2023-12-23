package adventofcode.y2023

import adventofcode.io.AdventSolution

fun main() {
    Day09.solve()
}

object Day09 : AdventSolution(2023, 9, "Mirage Maintenance") {

    override fun solvePartOne(input: String) = parse(input).sumOf { seq ->
        differences(seq).sumOf { it.last() }
    }

    override fun solvePartTwo(input: String) = parse(input).sumOf { seq ->
        differences(seq).map { it.first() }.toList().foldRight(0, Int::minus)
    }

    private fun parse(input: String) =
        input.lineSequence().map { it.split(" ").map(String::toInt) }

    private fun differences(seq: List<Int>) =
        generateSequence(seq) { it.zipWithNext { a, b -> b - a } }.takeWhile { row -> !row.all(0::equals) }
}
