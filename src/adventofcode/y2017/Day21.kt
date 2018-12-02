package adventofcode.y2017

import adventofcode.AdventSolution

private val startingConfiguration: Square = ".#./..#/###".split('/')

object Day21 : AdventSolution(2017, 21, "Fractal Art") {

	override fun solvePartOne(input: String) = puzzle(input, 5).toString()

	override fun solvePartTwo(input: String) = puzzle(input, 18).toString()

	private fun puzzle(input: String, i: Int): Int {
		val rules = parseRewriteRules(input)

		var grid = startingConfiguration
		repeat(i) {
			val squareSize = if (grid[0].length % 2 == 0) 2 else 3
			grid = grid.step(squareSize, rules)
		}

		return grid.countLights()
	}
}

private fun parseRewriteRules(input: String): Map<Square, Square> =
		input.split("\n")
				.map { it.split(" => ").map { it.split("/") } }
				.flatMap { (old, new) -> old.symmetries().map { symmetryOfOld -> symmetryOfOld to new } }
				.toMap()

private fun Square.symmetries(): List<Square> {
	val rotations = generateSequence(this) { it.rotate() }.take(4).asIterable()
	return rotations + rotations.map { it.reversed() }
}

private fun Square.rotate(): Square = indices.map { i ->
	this[0].indices.reversed()
			.map { j -> this[j][i] }
			.fold(StringBuilder(), StringBuilder::append)
			.toString()
}

private fun Square.step(squareSize: Int, fullRules: Map<Square, Square>): Square =
		chunked(squareSize).flatMap { chunkRow ->
			chunkRow.map { line -> line.chunked(squareSize) }
					.transpose()
					.map { square -> fullRules[square]!! }
					.transpose()
					.map { line -> line.fold(StringBuilder(), StringBuilder::append).toString() }
		}

private fun Square.countLights() = sumBy { line -> line.count { char -> char == '#' } }

private fun <T> List<List<T>>.transpose() =
		this[0].indices.map { col -> map { it[col] } }

private typealias Square = List<String>
