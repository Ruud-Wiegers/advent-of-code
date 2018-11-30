package adventofcode.y2015

import adventofcode.AdventSolution

object Day18 : AdventSolution(2015, 18, "Like a GIF For Your Yard") {

	override fun solvePartOne(input: String) = generateSequence(ConwayGrid(input)) { it.next() }
			.drop(100)
			.first()
			.countAlive()
			.toString()


	override fun solvePartTwo(input: String) = generateSequence(ConwayGrid(input).stuckCorners()) { it.next().stuckCorners() }
			.drop(100)
			.first()
			.countAlive()
			.toString()

}


private data class ConwayGrid(private val grid: List<BooleanArray>) {

	constructor(input: String) : this(input
			.split("\n")
			.map { it.map { it == '#' }.toBooleanArray() })

	fun next() = List(grid.size) { y ->
		BooleanArray(grid[0].size) { x ->
			shouldTurnAlive(x, y)
		}
	}.let { ConwayGrid(it) }

	fun stuckCorners() = this.copy().apply {
		val last = grid.first().size - 1
		grid.first()[0] = true
		grid.first()[last] = true
		grid.last()[0] = true
		grid.last()[last] = true
	}

	fun countAlive() = grid.sumBy { it.count { row -> row } }


	private fun shouldTurnAlive(x: Int, y: Int) = neighbors(x, y) == 3
			|| grid[y][x] && neighbors(x, y) == 2

	private fun neighbors(x: Int, y: Int) =
			(x - 1..x + 1).flatMap { i ->
				(y - 1..y + 1).mapNotNull { j ->
					if (i == x && j == y) false else grid.getOrNull(j)?.getOrNull(i)
				}
			}.count { it }

}




