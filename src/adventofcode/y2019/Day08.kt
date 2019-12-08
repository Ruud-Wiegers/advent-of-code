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
            .reduce(this::mergeLayer)
            .replace('1', '█')
            .replace('0', ' ')
            .chunked(25)
            .joinToString("\n", "\n")

    private fun mergeLayer(image: String, layer: String): String = image
            .zip(layer) { imagePixel, backgroundPixel ->
                if (imagePixel == '2') backgroundPixel else imagePixel
            }
            .joinToString("")

}
