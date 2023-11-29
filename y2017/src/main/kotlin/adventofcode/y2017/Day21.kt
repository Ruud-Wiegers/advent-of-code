package adventofcode.y2017

import adventofcode.io.AdventSolution
import adventofcode.util.transpose

private val startingConfiguration: Square = ".#./..#/###".split('/')

object Day21 : AdventSolution(2017, 21, "Fractal Art") {

	override fun solvePartOne(input: String) = puzzle(input, 5)

	override fun solvePartTwo(input: String) = puzzle(input, 18)

	private fun puzzle(input: String, i: Int): Int {
		val rules = parseRewriteRules(input)

		var grid = startingConfiguration
		repeat(i) {
			val squareSize = if (grid.size % 2 == 0) 2 else 3
			grid = grid.step(squareSize, rules)
		}

		return grid.countLights()
	}
}

private fun parseRewriteRules(input: String): Map<Square, Square> =
		input.lines()
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
					.map { square -> fullRules.getValue(square) }
					.transpose()
					.map { line -> line.fold(StringBuilder(), StringBuilder::append).toString() }
		}

private fun Square.countLights() = sumOf { line -> line.count { char -> char == '#' } }

private typealias Square = List<String>
