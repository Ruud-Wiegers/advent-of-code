package adventofcode.y2017

import adventofcode.AdventSolution

object Day13 : AdventSolution(2017, 13, "Packet Scanners") {

	override fun solvePartOne(input: String): String = parseInput(input)
			.filter { (depth, range) -> depth % (range * 2 - 2) == 0 }
			.entries
			.sumOf { (depth, range) -> depth * range }
			.toString()

	override fun solvePartTwo(input: String): String {
		val lasers = parseInput(input)
		val shortestSafeDelay = generateSequence(0) { it + 1 }
				.indexOfFirst { delay ->
					lasers.none { (depth, range) ->
						(depth + delay) % (range * 2 - 2) == 0
					}
				}
		return shortestSafeDelay.toString()
	}

	private fun parseInput(input: String) = input.lines()
			.map { it.split(": ") }
			.associate { (depth, range) -> depth.toInt() to range.toInt() }
}
