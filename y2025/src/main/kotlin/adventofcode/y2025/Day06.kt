package adventofcode.y2025

import adventofcode.io.AdventSolution
import adventofcode.util.algorithm.transposeString
import adventofcode.util.collections.splitAfter
import adventofcode.util.collections.splitBefore
import kotlin.math.abs

fun main() {
    Day06.solve()
}

object Day06 : AdventSolution(2025, 6, "Trash Compactor") {

    override fun solvePartOne(input: String): Any {

        return parse(input).sumOf {
            when(it.operator) {
                '+' -> it.numbers.sum()
                '*' -> it.numbers.reduce(Long::times)
                else -> throw IllegalArgumentException("Unknown operator ${it.operator}")
            }
        }
    }

    override fun solvePartTwo(input: String): Int {
        return TODO()
    }

    private fun parse(input: String): List<Problem> {
        val lines = input.lines()

        val padTo = lines.maxOf { it.length }
        val padded = lines.map { it.padEnd(padTo, ' ') }

        return padded.transposeString()
            .splitBefore { it.last() in "*+" }
            .map { parseToProblem(it.transposeString()) }
    }

    private fun parseToProblem(input: List<String>): Problem {
        val numbers = input.dropLast(1).map { it.trim().toLong()}
        val operator = input.last().trim()[0]

        return Problem(numbers,operator)
    }
}

private data class Problem(val numbers: List<Long>, val operator: Char)