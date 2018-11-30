package adventofcode.y2015

import adventofcode.AdventSolution

object Day02 : AdventSolution(2015, 2, "I Was Told There Would Be No Math") {

	override fun solvePartOne(input: String) = toListOfPresents(input)
			.map { 3 * it[0] * it[1] + 2 * it[1] * it[2] + 2 * it[2] * it[0] }
			.sum()
			.toString()

	override fun solvePartTwo(input: String) = toListOfPresents(input)
			.map { 2 * (it[0] + it[1]) + it[0] * it[1] * it[2] }
			.sum()
			.toString()

	private fun toListOfPresents(input: String): List<List<Int>> = input.split('\n')
			.filter(String::isNotBlank)
			.map { it.split("x").map { it.toInt() }.sorted() }

}