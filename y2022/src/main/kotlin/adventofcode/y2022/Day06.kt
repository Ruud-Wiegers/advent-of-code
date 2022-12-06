package adventofcode.y2022

import adventofcode.AdventSolution

object Day06 : AdventSolution(2022, 6, "Tuning Trouble") {

    override fun solvePartOne(input: String) = input.indexOfMarker(4)

    override fun solvePartTwo(input: String) = input.indexOfMarker(14)

    private fun String.indexOfMarker(markerSize: Int) =
        asSequence().windowed(markerSize).indexOfFirst { it.toSet().size == markerSize } + markerSize
}
