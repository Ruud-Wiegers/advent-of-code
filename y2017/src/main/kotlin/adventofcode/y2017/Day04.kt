package adventofcode.y2017

import adventofcode.AdventSolution

object Day04 : AdventSolution(2017, 4, "High-Entropy Passphrases") {

	override fun solvePartOne(input: String) = input
			.lines()
			.map { it.split(" ") }
			.count { it.distinct().size == it.size }
			.toString()

	override fun solvePartTwo(input: String) = input
			.lines()
			.map { it.split(" ").map { it.toList().sorted() } }
			.count { it.distinct().size == it.size }
			.toString()
}
