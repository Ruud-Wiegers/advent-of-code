package nl.ruudwiegers.adventofcode.y2015

import adventofcode.AdventSolution
import nl.ruudwiegers.adventofcode.util.md5

object Day04 : AdventSolution(2015, 4, "The Ideal Stocking Stuffer") {

	override fun solvePartOne(input: String) = solve(input, "00000")

	override fun solvePartTwo(input: String) = solve(input, "000000")

	private fun solve(input: String, prefix: String): String {

		return generateSequence(0) { it + 1 }
				.map { input + it }
				.map { md5(it) }
				.withIndex()
				.first { it.value.startsWith(prefix) }
				.index.toString()
	}

}