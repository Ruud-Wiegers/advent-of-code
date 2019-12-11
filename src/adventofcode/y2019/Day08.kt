package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day08.solve()

object Day08 : AdventSolution(2019, 8, "Space Image Format") {

    override fun solvePartOne(input: String) = input
            .chunked(6 * 25)
            .map { layer -> layer.groupingBy { it }.eachCount() }
            .minBy { it['0'] ?: 0 }
            ?.let { (it['1'] ?: 0) * (it['2'] ?: 0) }

    override fun solvePartTwo(input: String) = input
            .chunked(6 * 25)
            .reduce(this::mergeLayers)
            .map { if (it == '1') "██" else "  " }
            .chunked(25)
            .joinToString(separator = "\n", prefix = "\n") { it.joinToString("") }

    private fun mergeLayers(foreground: String, background: String): String =
            foreground.zip(background, this::mergePixels).joinToString("")

    private fun mergePixels(foreground: Char, background: Char): Char =
            if (foreground != '2') foreground else background

}
