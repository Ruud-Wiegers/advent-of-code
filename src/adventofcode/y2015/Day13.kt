package adventofcode.y2015

import adventofcode.AdventSolution
import adventofcode.util.collections.permutations


object Day13 : AdventSolution(2015, 13, "Knights of the Dinner Table") {

	override fun solvePartOne(input: String) = tryAllRoutes(input).max().toString()

	override fun solvePartTwo(input: String) = tryAllRoutes2(input).max().toString()

	private fun tryAllRoutes(input: String): Sequence<Int> {
		val distanceMap = parseInput(input)
		val locations = distanceMap.keys.flatMap { listOf(it.first, it.second) }.toSet().toList()
		val distances = buildDistanceMatrix(locations, distanceMap)
		return routes(locations, distances)
	}

	private fun tryAllRoutes2(input: String): Sequence<Int> {
		val distanceMap = parseInput(input)
		val locations = distanceMap.keys.flatMap { listOf(it.first, it.second) }.toSet().toList() + "me"
		val distances = buildDistanceMatrix(locations, distanceMap)
		return routes(locations, distances)
	}

	private fun buildDistanceMatrix(locations: List<String>, distanceMap: Map<Pair<String, String>, Int>) =
			locations.map { start ->
				locations.map { end ->
					distanceMap[start to end]
							?: 0
				}
			}


	private fun routes(locations: List<String>, distanceTable: List<List<Int>>) =
			(1 until locations.size).permutations()
					.map { listOf(0) + it + 0 }
					.map { it.zipWithNext { a, b -> distanceTable[a][b] + distanceTable[b][a] }.sum() }


	private fun parseInput(distances: String) = distances.lineSequence()
			.mapNotNull { Regex("(\\w+) would (\\w+) (\\d+) happiness units by sitting next to (\\w+).").matchEntire(it) }
			.map { it.destructured }
			.map { (start, sign, amount, end) ->
				val distance = if (sign == "gain") amount.toInt() else -amount.toInt()
				start to end to distance
			}
			.toMap()


}