package adventofcode.y2016

import adventofcode.AdventSolution

object Day02 : AdventSolution(2016, 2, "Bathroom Security") {

	override fun solvePartOne(input: String) = solve(input, Day02::stepOnSquarePad)

	override fun solvePartTwo(input: String) = solve(input, Day02::stepOnDiamondPad)

	private fun solve(input: String, function: (Int, Char) -> Int): String = input
			.lineSequence()
			.fold(listOf(5)) { result, line ->
				result + line.asSequence().fold(result.last(), function)
			}
			.joinToString("") { it: Int -> it.toString(16).toUpperCase() }
			.drop(1)


	private fun stepOnSquarePad(button: Int, instruction: Char): Int = when (instruction) {
		'U' -> when (button) {
			1, 2, 3 -> button
			else -> button - 3
		}
		'D' -> when (button) {
			7, 8, 9 -> button
			else -> button + 3
		}
		'L' -> when (button) {
			1, 4, 7 -> button
			else -> button - 1
		}
		'R' -> when (button) {
			3, 6, 9 -> button
			else -> button + 1
		}
		else -> throw IllegalArgumentException("not an instruction:$instruction")
	}

	private fun stepOnDiamondPad(button: Int, instruction: Char): Int = when (instruction) {
		'U' -> up(button)
		'D' -> down(button)
		'L' -> left(button)
		'R' -> right(button)
		else -> button
	}


	private fun up(button: Int): Int = when (button) {
		3, 13 -> button - 2
		in 6..8, in 10..12 -> button - 4
		else -> button
	}

	private fun down(button: Int): Int = when (button) {
		1, 11 -> button + 2
		in 2..4, in 6..8 -> button + 4
		else -> button
	}

	private fun left(button: Int): Int = when (button) {
		3, 4, in 6..9, 11, 12 -> button - 1
		else -> button
	}

	private fun right(button: Int): Int = when (button) {
		2, 3, in 5..8, 10, 11 -> button + 1
		else -> button
	}
}