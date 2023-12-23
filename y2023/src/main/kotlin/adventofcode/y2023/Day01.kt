package adventofcode.y2023

import adventofcode.io.AdventSolution

fun main() {
    Day01.solve()
}

object Day01 : AdventSolution(2023, 1, "Trebuchet?!") {

    override fun solvePartOne(input: String) = solve(input, digitValues)
    override fun solvePartTwo(input: String) = solve(input, digitValues + wordValues)


    private fun solve(input: String, numericValues: Map<String, Int>) = input
        .lineSequence()
        .sumOf {
            val (_, firstDigit) = it.findAnyOf(numericValues.keys)!!
            val firstValue = numericValues.getValue(firstDigit)

            val (_, lastDigit) = it.findLastAnyOf(numericValues.keys)!!
            val lastValue = numericValues.getValue(lastDigit)

            firstValue * 10 + lastValue
        }
}


private val digitValues = (0..9).associateBy(Int::toString)


private val wordValues = listOf(
    "zero",
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine"
).withIndex().associate { it.value to it.index }