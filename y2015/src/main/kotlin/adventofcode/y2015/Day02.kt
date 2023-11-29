package adventofcode.y2015

import adventofcode.io.AdventSolution

object Day02 : AdventSolution(2015, 2, "I Was Told There Would Be No Math") {

	override fun solvePartOne(input: String) =
		toListOfPresents(input).sumOf { 3 * it[0] * it[1] + 2 * it[1] * it[2] + 2 * it[2] * it[0] }
		.toString()

	override fun solvePartTwo(input: String) =
		toListOfPresents(input).sumOf { 2 * (it[0] + it[1]) + it[0] * it[1] * it[2] }
		.toString()

	private fun toListOfPresents(input: String): List<List<Int>> = input.lines()
			.filter(String::isNotBlank)
			.map { it.split("x").map(String::toInt).sorted() }

}