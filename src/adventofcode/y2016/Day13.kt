package adventofcode.y2016

import adventofcode.AdventSolution

object Day13 : AdventSolution(2016, 13, "Maze") {

	var magicNumber: Int = 0

	override fun solvePartOne(input: String): Int {
		magicNumber = input.toInt()

		return generateSequence(Solver(setOf(Cubicle(1, 1))), Solver::next)
				.takeWhile { Cubicle(31, 39) !in it.edge }
				.count()
	}

	override fun solvePartTwo(input: String): Int {
		magicNumber = input.toInt()

		return generateSequence(Solver(setOf(Cubicle(1, 1))), Solver::next)
				.take(52)
				.last()
				.visited
				.size
	}


	data class Solver(val edge: Set<Cubicle>, val visited: Set<Cubicle> = emptySet()) {
		fun next() = Solver(
				edge = edge.asSequence().flatMap(Cubicle::getNeighbors).filterNot { it in visited }.toSet(),
				visited = visited + edge
		)
	}

	data class Cubicle(val x: Int, val y: Int) {
		fun getNeighbors() = sequenceOf(
				copy(x = x + 1),
				copy(x = x - 1),
				copy(y = y + 1),
				copy(y = y - 1))
				.filter(Cubicle::isOpen)

		private fun isOpen(): Boolean = x >= 0
				&& y >= 0
				&& Integer.bitCount(x * x + 3 * x + 2 * x * y + y + y * y + magicNumber) % 2 == 0
	}
}
