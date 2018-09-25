package nl.ruudwiegers.adventofcode.y2015

import adventofcode.AdventSolution

object Day05 : AdventSolution(2015, 5, "Doesn't He Have Intern-Elves For This?") {

	override fun solvePartOne(input: String) = input.split('\n')
			.filter { it.count { it in "aeiou" } >= 3 }
			.filter { it.zipWithNext().any { it.first == it.second } }
			.filterNot { "ab" in it || "cd" in it || "pq" in it || "xy" in it }
			.count()
			.toString()


	override fun solvePartTwo(input: String) = input.split('\n')
			.filter { it.windowed(3).any { it[0] == it[2] } }
			.filter { string ->
				string.zipWithNext()
						.withIndex()
						.any { (i, v) ->
							string.substring(i + 2).zipWithNext().any { other -> v.first == other.first && v.second == other.second }
						}
			}
			.count().toString()
}