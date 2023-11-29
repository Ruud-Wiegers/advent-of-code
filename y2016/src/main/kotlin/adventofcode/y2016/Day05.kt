package adventofcode.y2016

import adventofcode.io.AdventSolution
import adventofcode.util.algorithm.md5

object Day05 : AdventSolution(2016, 5, "How About a Nice Game of Chess?") {

	override fun solvePartOne(input: String) = generateInterestingHashes(input)
			.take(8)
			.map { it[5] }
			.joinToString("")

	override fun solvePartTwo(input: String) = generateInterestingHashes(input)
			.distinctBy { it[5] }
			.filter { it[5] in '0'..'7' }
			.take(8)
			.sortedBy { it[5] }
			.map { it[6] }
			.joinToString("")

	private fun generateInterestingHashes(input: String): Sequence<String> =
			generateSequence(0) { it + 1 }
					.map { input + it }
					.map { md5(it) }
					.filter { it.startsWith("00000") }

}

