package adventofcode.y2016

import adventofcode.AdventSolution


object Day18 : AdventSolution(2016, 18, "Like a Rogue") {
	override fun solvePartOne(input: String) = solve(input, 40)
	override fun solvePartTwo(input: String) = solve(input, 400000)

	private fun solve(firstLine: String, lines: Int): String {
		val room = generateSequence(firstLine, ::nextLine)
		val safeSpaces = room
				.take(lines)
				.sumOf { line -> line.count { it == '.' } }

		return safeSpaces.toString()
	}


	private fun nextLine(line: String): String = ".$line.".windowed(3) {
		when (it) {
			"^..", "^^.", ".^^", "..^" -> '^'
			else -> '.'
		}
	}
			.fold(StringBuilder(), StringBuilder::append)
			.toString()
}