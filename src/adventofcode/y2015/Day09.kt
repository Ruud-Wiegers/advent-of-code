package adventofcode.y2015

import adventofcode.AdventSolution
import adventofcode.util.permute


object Day09 : AdventSolution(2015, 9, "All in a Single Night") {

	override fun solvePartOne(input: String) = tryAllRoutes(input).min().toString()

	override fun solvePartTwo(input: String) = tryAllRoutes(input).max().toString()

	private fun tryAllRoutes(input: String): Sequence<Int> {
		val distanceMap = parseInput(input)
		val locations = distanceMap.keys.flatMap { listOf(it.first, it.second) }.toSet().toList()
		val distances = buildDistanceMatrix(locations, distanceMap)
		return routes(locations, distances)
	}

	private fun buildDistanceMatrix(locations: List<String>, distanceMap: Map<Pair<String, String>, Int>) =
			locations.map { start ->
				locations.map { end ->
					distanceMap[start to end]
							?: distanceMap[end to start]
							?: 0
				}
			}


	private fun routes(locations: List<String>, distanceTable: List<List<Int>>) =
			permute(locations.indices.toList())
					.map { it.zipWithNext { a, b -> distanceTable[a][b] }.sum() }


	private fun parseInput(distances: String) = distances.lineSequence()
			.mapNotNull { Regex("([a-zA-Z]+) to ([a-zA-Z]+) = (\\d+)").matchEntire(it) }
			.map { it.destructured }
			.map { (start, end, distance) -> start to end to distance.toInt() }
			.toMap()


}