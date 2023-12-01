package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() {
    Day01.solve()
}

object Day01 : AdventSolution(2023, 1, "Trebuchet?!") {

    override fun solvePartOne(input: String) = solve(input, p1)

    override fun solvePartTwo(input: String) = solve(input, p2)

    private fun solve(input: String, numericValues: Map<String, Int>) = input
        .lineSequence()
        .sumOf {
            val (_, firstDigit) = it.findAnyOf(numericValues.keys)!!
            val (_, lastDigit) = it.findLastAnyOf(numericValues.keys)!!
            numericValues.getValue(firstDigit) * 10 + numericValues.getValue(lastDigit)
        }
}


private val p1 = (0..9).associateBy(Int::toString)

private val p2 = p1 + mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)