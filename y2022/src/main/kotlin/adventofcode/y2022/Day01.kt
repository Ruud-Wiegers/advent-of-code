package adventofcode.y2022

import adventofcode.AdventSolution

object Day01 : AdventSolution(2022, 1, "Calorie Counting") {

    override fun solvePartOne(input: String) = sortedCalories(input).first()

    override fun solvePartTwo(input: String) = sortedCalories(input).take(3).sum()

    private fun sortedCalories(input: String) = input
        .splitToSequence("\n\n")
        .map { it.lineSequence().sumOf(String::toInt) }
        .sortedDescending()
}
