package adventofcode.y2015

import adventofcode.AdventSolution

object Day25 : AdventSolution(2015, 25, "Let It Snow") {


	override fun solvePartOne(input: String): String {
		val (row, column) = readPosition(input)
		val n = convertToIndex(row, column)
		return generateSequence(20151125L) { it * 252533L % 33554393 }
				.take(n)
				.last()
				.toString()
	}

	private fun readPosition(input: String): Pair<Int, Int> {
		val row = input.substringAfter("row ").substringBefore(",").toInt()
		val column = input.substringAfter("column ").substringBefore(".").toInt()
		return row to column
	}

	private fun convertToIndex(row: Int, column: Int): Int = triangle(row + column - 1) - row + 1
	private fun triangle(n: Int) = (n * (n + 1)) / 2

	override fun solvePartTwo(input: String) = "Gratis!"
}



