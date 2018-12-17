package adventofcode.y2015

import adventofcode.AdventSolution
import adventofcode.util.md5

object Day04 : AdventSolution(2015, 4, "The Ideal Stocking Stuffer") {

	override fun solvePartOne(input: String) = solve(input, "00000")

	override fun solvePartTwo(input: String) = solve(input, "000000")

	private fun solve(input: String, prefix: String) =
			generateSequence(0, Int::inc)
					.map { input + it }
					.map { md5(it) }
					.indexOfFirst { it.startsWith(prefix) }
					.toString()

}