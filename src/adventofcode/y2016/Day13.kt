package adventofcode.y2016

import adventofcode.AdventSolution

object Day13 : AdventSolution(2016, 13, "Maze") {

	var magicNumber: Int = 0

	override fun solvePartOne(input: String): String {
		magicNumber = input.toInt()
		var openList = setOf(MazeState(1, 1))
		val closedList = mutableSetOf<MazeState>()
		var count = 0
		while (openList.none { it.x == 31 && it.y == 39 }) {
			val new = openList.asSequence().flatMap(MazeState::getNeighbors).filterNot { it in closedList }.toSet()
			closedList += openList
			openList = new
			count++
		}
		return count.toString()
	}

	override fun solvePartTwo(input: String): String {
		magicNumber = input.toInt()
		var openList = setOf(MazeState(1, 1))
		val closedList = mutableSetOf<MazeState>()
		repeat(50) {
			val new = openList.asSequence().flatMap(MazeState::getNeighbors).filterNot { it in closedList }.toSet()
			closedList += openList
			openList = new
		}
		return (closedList.size + openList.size).toString()
	}


	data class MazeState(val x: Int, val y: Int) {
		fun getNeighbors() = sequenceOf(
				copy(x = x + 1),
				copy(x = x - 1),
				copy(y = y + 1),
				copy(y = y - 1))
				.filter(MazeState::isValid)

		private fun isValid(): Boolean = x >= 0
				&& y >= 0
				&& Integer.bitCount(x * x + 3 * x + 2 * x * y + y + y * y + magicNumber) % 2 == 0

	}
}
