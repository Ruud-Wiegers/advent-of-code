package adventofcode.y2016

import adventofcode.AdventSolution


object Day06 : AdventSolution(2016, 6, "Signals and Noise") {

	override fun solvePartOne(input: String): String {
		val lines = input.split("\n")


		return lines.first().indices.map { index ->
			lines
					.map { it[index] }
					.groupingBy { it }
					.eachCount()
					.entries
					.maxBy { it.value }
					?.key
		}
				.joinToString("")
	}

	override fun solvePartTwo(input: String): String {
		val lines = input.split("\n")


		return lines.first().indices.map { index ->
			lines
					.map { it[index] }
					.groupingBy { it }
					.eachCount()
					.entries
					.minBy { it.value }
					?.key
		}
				.joinToString("")
	}
}
