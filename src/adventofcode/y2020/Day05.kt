package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day05.solve()

object Day05 : AdventSolution(2020, 5, "Binary Boarding")
{

    override fun solvePartOne(input: String) = input
        .lineSequence()
        .map(::parse)
        .map { it.id }
        .maxOrNull()

    override fun solvePartTwo(input: String) = input
        .lineSequence()
        .map(::parse)
        .map { it.id }
        .sorted()
        .zipWithNext()
        .first { (a, b) -> b - a == 2 }
        .let { it.first + 1 }

    private val regex = "([FB]{7})([LR]{3})".toRegex()

    private fun parse(input: String): BoardingPass
    {
        val (rowStr, colStr) = regex.matchEntire(input)!!.destructured

        val row = rowStr.asSequence().fold(0) { acc, c -> acc * 2 + if (c == 'B') 1 else 0 }
        val col = colStr.asSequence().fold(0) { acc, c -> acc * 2 + if (c == 'R') 1 else 0 }
        return BoardingPass(row, col)
    }

    private data class BoardingPass(val row: Int, val column: Int)
    {
        val id = row * 8 + column
    }
}
