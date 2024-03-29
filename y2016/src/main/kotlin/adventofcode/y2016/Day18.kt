package adventofcode.y2016

import adventofcode.io.AdventSolution


object Day18 : AdventSolution(2016, 18, "Like a Rogue") {
	override fun solvePartOne(input: String) = solve(input, 40)
	override fun solvePartTwo(input: String) = solve(input, 400000)

	private fun solve(firstLine: String, lines: Int)=
		generateSequence(firstLine, ::nextLine)
				.take(lines)
				.sumOf { line -> line.count { it == '.' } }

	private fun nextLine(line: String): String = ".$line.".windowed(3) {
		when (it) {
			"^..", "^^.", ".^^", "..^" -> '^'
			else -> '.'
		}
	}
			.fold(StringBuilder(), StringBuilder::append)
			.toString()
}