package adventofcode.y2017

import adventofcode.AdventSolution

object Day09 : AdventSolution(2017, 9, "Stream Processing") {

	override fun solvePartOne(input: String): String {
		val cleanedInput = input
				.replace("!.".toRegex(), "")
				.replace("<.*?>".toRegex(), "")

		var depth = 0
		var score = 0
		for (it in cleanedInput) when (it) {
			'{' -> depth++
			'}' -> score += depth--
		}

		return score.toString()
	}

	override fun solvePartTwo(input: String): String {
		val unignored = input.replace("!.".toRegex(), "")
		val cleaned = unignored.replace("<.*?>".toRegex(), "<>")
		return (unignored.length - cleaned.length).toString()
	}
}

