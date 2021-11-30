package adventofcode.y2017

import adventofcode.AdventSolution

object Day01 : AdventSolution(2017, 1, "Inverse Captcha") {

	override fun solvePartOne(input: String): String {
		val consecutiveDigitPairs = (input + input[0]).zipWithNext()
		return CoordinateMatchingPairs(consecutiveDigitPairs)
	}

	override fun solvePartTwo(input: String): String {
		val shiftedInput = input.shiftRight(input.length / 2)
		val digitPairs = input.zip(shiftedInput)
		return CoordinateMatchingPairs(digitPairs)
	}

	private fun CoordinateMatchingPairs(pairs: List<Pair<Char, Char>>): String = pairs
			.filter { (a, b) -> a == b }
			.sumOf { Character.getNumericValue(it.first) }
			.toString()

	private fun String.shiftRight(n: Int) = substring(n) + substring(0 until n)
}
