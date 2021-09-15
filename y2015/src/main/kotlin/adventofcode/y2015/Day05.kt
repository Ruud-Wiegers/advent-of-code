package adventofcode.y2015

import adventofcode.AdventSolution

object Day05 : AdventSolution(2015, 5, "Doesn't He Have Intern-Elves For This?") {

	override fun solvePartOne(input: String) = input.lineSequence()
			.filter { it.count { it in "aeiou" } >= 3 }
			.filter { it.zipWithNext().any { it.first == it.second } }
			.filterNot { "ab" in it || "cd" in it || "pq" in it || "xy" in it }
			.count()


	override fun solvePartTwo(input: String) = input
			.lineSequence()
			.filter { it.windowed(3).any { it[0] == it[2] } }
			.filter { string ->
				string.windowedSequence(2)
						.withIndex()
						.any { (i, v) -> v in string.substring(i + 2) }
			}
			.count()
}