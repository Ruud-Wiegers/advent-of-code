package adventofcode.y2016

import adventofcode.AdventSolution
import nl.ruudwiegers.adventofcode.util.IState
import nl.ruudwiegers.adventofcode.util.aStar
import nl.ruudwiegers.adventofcode.util.permute
import kotlin.math.abs

object Day24 : AdventSolution(2016, 24, "Air Duct Spelunking") {

	lateinit var maze: List<BooleanArray>
	lateinit var checkPoints: Array<Pair<Int, Int>>

	override fun solvePartOne(input: String): String {
		val distanceTable = buildDistanceTable(input)

		val checkpointPermutations = permute((1..7).toList())
		val routes = checkpointPermutations.map { listOf(0) + it }
		val costOfRoutes = routes.map { it.zipWithNext { a, b -> distanceTable[a][b] }.sum() }
		val cheapestRoute = costOfRoutes.min()

		return cheapestRoute.toString()
	}

	override fun solvePartTwo(input: String): String {
		val distanceTable = buildDistanceTable(input)

		val checkpointPermutations = permute((1..7).toList())
		val routes = checkpointPermutations.map { listOf(0) + it + listOf(0) }
		val costOfRoutes = routes.map { it.zipWithNext { a, b -> distanceTable[a][b] }.sum() }
		val cheapestRoute = costOfRoutes.min()

		return cheapestRoute.toString()
	}


	private fun buildDistanceTable(input: String): List<List<Int>> {
		val lines = input.split("\n")

		maze = lines.map { row -> row.map { it != '#' }.toBooleanArray() }

		checkPoints = buildCheckpoints(lines)

		return checkPoints.indices.map { start ->
			checkPoints.indices.map { end ->
				val path = aStar(MazeState(checkPoints[start], checkPoints[end])) ?: throw IllegalStateException()
				path.cost
			}
		}
	}

	data class MazeState(private val x: Int,
						 private val y: Int,
						 private val gX: Int,
						 private val gY: Int) : IState {

		constructor(start: Pair<Int, Int>, goal: Pair<Int, Int>) : this(start.first, start.second, goal.first, goal.second)

		override val isGoal: Boolean
			get() = x == gX && y == gY

		override val heuristic: Int
			get() = abs(gX - x) + abs(gY - y)

		override fun getNeighbors() = sequenceOf(
				copy(y = y - 1),
				copy(y = y + 1),
				copy(x = x - 1),
				copy(x = x + 1)
		).filter { maze[it.y][it.x] }
	}

	private fun buildCheckpoints(lines: List<String>): Array<Pair<Int, Int>> {
		val checkpoints = mutableMapOf<Int, Pair<Int, Int>>()
		lines.forEachIndexed { y, line ->
			line.forEachIndexed { x, ch ->
				if (ch in '0'..'7')
					checkpoints[ch.toString().toInt()] = Pair(x, y)
			}
		}

		return Array(8) { checkpoints[it] ?: throw IllegalStateException() }
	}
}