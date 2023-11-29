package adventofcode.y2017

import adventofcode.io.AdventSolution

object Day12 : AdventSolution(2017, 12, "Digital Plumber") {

	override fun solvePartOne(input: String): String {
		val connections = parseInput(input)
		val group = findGroup(connections, 0)

		return group.size.toString()
	}

	override fun solvePartTwo(input: String): String {
		val connections: MutableMap<Int, List<Int>> = parseInput(input).toMutableMap()
		val groups = generateSequence {
			connections.keys.firstOrNull()
					?.let { id -> findGroup(connections, id) }
					?.also { group -> connections -= group }
		}

		return groups.count().toString()
	}

	private fun parseInput(input: String): Map<Int, List<Int>> = input.lines()
			.map { line -> line.split(" <-> ") }
			.associate { (program, connections) ->
				program.toInt() to connections.split(", ").map { it.toInt() }
			}

	private fun findGroup(connections: Map<Int, List<Int>>, firstElement: Int): Set<Int> {
		val group = mutableSetOf<Int>()
		var newConnections = setOf(firstElement)

		while (newConnections.isNotEmpty()) {
			group += newConnections
			newConnections = newConnections.flatMap { connections[it]!! }.toSet() - group
		}
		return group
	}
}
