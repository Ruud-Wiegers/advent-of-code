package adventofcode.y2017

import adventofcode.AdventSolution

object Day24 : AdventSolution(2017, 24, "Electromagnetic Moat") {

	override fun solvePartOne(input: String): String {
        val pairs = parseInput(input)

        return buildAllBridges(emptyList(), pairs, 0)
            .map { it.sumOf { (a, b) -> a + b } }
            .maxOrNull()
            .toString()
    }

	override fun solvePartTwo(input: String): String {
		val pairs = parseInput(input)

		return buildAllBridges(emptyList(), pairs, 0)
				.map { it.size to it.sumOf { (a, b) -> a + b } }
				.maxWithOrNull(compareBy({ it.first }, { it.second }))
				?.second
				.toString()
	}

	private fun buildAllBridges(bridge: List<Pair<Int, Int>>, available: List<Pair<Int, Int>>, last: Int): List<List<Pair<Int, Int>>> = available
			.filter { it.first == last || it.second == last }
			.flatMap { buildAllBridges(bridge + it, available - it, if (last == it.first) it.second else it.first) }
			.let { it.ifEmpty { listOf(bridge) } }


	private fun parseInput(input: String): List<Pair<Int, Int>> = input
			.lines()
			.map { it.split("/") }
			.map { it[0].toInt() to it[1].toInt() }

}
