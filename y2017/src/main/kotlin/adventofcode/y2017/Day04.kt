package adventofcode.y2017

import adventofcode.io.AdventSolution

object Day04 : AdventSolution(2017, 4, "High-Entropy Passphrases") {

	override fun solvePartOne(input: String) = input
			.lines()
			.map { it.split(" ") }
			.count { it.distinct().size == it.size }
			.toString()

	override fun solvePartTwo(input: String) = input
			.lines()
			.map { it.split(" ").map(String::toList).map(List<Char>::sorted) }
			.count { it.distinct().size == it.size }
			.toString()
}
