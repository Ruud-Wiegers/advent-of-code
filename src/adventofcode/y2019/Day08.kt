package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day08.solve()

object Day08 : AdventSolution(2019, 8, "Space Image Format") {

    override fun solvePartOne(input: String) = input
            .chunked(6 * 25)
            .map { l -> l.groupingBy { it }.eachCount() }
            .minBy { it.getValue('0') }
            ?.let { it.getValue('1') * it.getValue('2') }


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
