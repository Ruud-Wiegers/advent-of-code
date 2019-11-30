package adventofcode.y2016

import adventofcode.AdventSolution


object Day22 : AdventSolution(2016, 22, "Grid Computing") {

	override fun solvePartOne(input: String): String {
		val nodes = parseInput(input)

		return nodes.sumBy { source ->
			nodes.count { target ->
				(source.x != target.x || source.y != target.y)
						&& source.used > 0
						&& source.used <= target.available
			}
		}.toString()
	}

	override fun solvePartTwo(input: String): String =
			parseInput(input)
					.sortedBy { it.x }
					.groupingBy { it.y }
					.fold("") { str, n ->
						str + when {
							n.used == 0 -> "O"
							n.size in 1..100 -> "_"
							else -> "#"
						}
					}
					.values
					.joinToString("\n")

	private fun parseInput(input: String): List<Node> = input
			.lines()
			.drop(2)
			.map { it.split(Regex("\\s+")) }
			.map {


				Node(matchNodeName(it[0]).first, matchNodeName(it[0]).second,
						it[1].dropLast(1).toInt(),
						it[2].dropLast(1).toInt(),
						it[3].dropLast(1).toInt())
			}


	private fun matchNodeName(descriptor: String): Pair<Int, Int> {
		val (x, y) = Regex("/dev/grid/node-x(\\d+)-y(\\d+)").matchEntire(descriptor)!!.destructured
		return Pair(x.toInt(), y.toInt())

	}

	private data class Node(val x: Int,
							val y: Int,
							val size: Int,
							val used: Int,
							val available: Int
	)
}