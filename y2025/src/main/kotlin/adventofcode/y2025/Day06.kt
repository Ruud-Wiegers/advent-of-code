package adventofcode.y2025

import adventofcode.io.AdventSolution
import adventofcode.util.algorithm.transposeString
import adventofcode.util.collections.splitBefore

fun main() {
    Day06.solve()
}

object Day06 : AdventSolution(2025, 6, "Trash Compactor") {

    override fun solvePartOne(input: String) = solve(input) { parseToProblem(it.transposeString()) }
    override fun solvePartTwo(input: String) = solve(input) { parseToTransposedProblem(it) }

    private fun solve(input: String, mapping: (List<String>) -> Problem): Long {
        val lines = input.lines()
        val padTo = lines.maxOf { it.length }
        val padded = lines.map { it.padEnd(padTo, ' ') }
        return padded.transposeString()
            .splitBefore { it.last() in "*+" }
            .map { mapping(it) }
            .sumOf {
                when (it.operator) {
                    '+' -> it.numbers.sum()
                    '*' -> it.numbers.reduce(Long::times)
                    else -> error("")
                }
            }
    }
}

private fun parseToProblem(input: List<String>): Problem {
    val numbers = input.dropLast(1).map { it.trim().toLong() }
    val operator = input.last().trim()[0]

    return Problem(numbers, operator)
}

private fun parseToTransposedProblem(input: List<String>): Problem {
    val numbers = input.filter { it.isNotBlank() }.map { it.dropLast(1).trim().toLong() }
    val operator = input.first().last()

    return Problem(numbers, operator)
}

private data class Problem(val numbers: List<Long>, val operator: Char)