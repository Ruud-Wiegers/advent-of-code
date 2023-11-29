package adventofcode.y2023

import adventofcode.io.AdventSolution

object Day01 : AdventSolution(2023, 1, "sdfg") {

    override fun solvePartOne(input: String) :Any {
        val parsed = parse(input)


        return "sdf"
    }
    override fun solvePartTwo(input: String) :Any {
        val parsed = parse(input)


        return "sdf"
    }
    private fun parse(input: String) = input
        .splitToSequence("\n\n")
        .map { it.lineSequence().sumOf(String::toInt) }
        .sortedDescending()
}
