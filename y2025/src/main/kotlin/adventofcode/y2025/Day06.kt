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

    private fun solve(input: String, mapping: (transposedInput: List<String>) -> Problem): Long {
        val lines = input.lines()
        val padTo = lines.maxOf { it.length }
        val padded = lines.map { it.padEnd(padTo, ' ') }

        val problems = padded.transposeString()
            .splitBefore { it.last() in "*+" }
            .map { mapping(it) }

        return problems.sumOf(Problem::solve)
    }
}

private fun parseToProblem(input: List<String>) = Problem(
    numbers = input.dropLast(1).map { it.trim().toLong() },
    operator = input.last().trim().first()
)

private fun parseToTransposedProblem(transposedInput: List<String>) = Problem(
    numbers = transposedInput.filter { it.isNotBlank() }.map { it.dropLast(1).trim().toLong() },
    operator = transposedInput.first().last()
)

private data class Problem(val numbers: List<Long>, val operator: Char) {
    fun solve() = when (operator) {
        '+' -> numbers.reduce(Long::plus)
        '*' -> numbers.reduce(Long::times)
        else -> error(operator)
    }
}