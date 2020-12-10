package adventofcode.y2016

import adventofcode.AdventSolution


object Day03 : AdventSolution(2016,3,"Squares With Three Sides") {
	override fun solvePartOne(input: String) = solve(input) {
		chunked(3) { Triangle(it[0], it[1], it[2]) }
	}

	override fun solvePartTwo(input: String) = solve(input) {
		chunked(9) {
			listOf(Triangle(it[0], it[3], it[6]),
					Triangle(it[1], it[4], it[7]),
					Triangle(it[2], it[5], it[8]))
		}.flatten()
	}

	private fun solve(input: String, mapToTriangles: Sequence<Int>.() -> Sequence<Triangle>) = input
			.splitToSequence("\n", " ")
			.filterNot(String::isBlank)
			.map(String::toInt)
			.mapToTriangles()
			.count(Triangle::isValid)
			.toString()

	private data class Triangle(val a: Int, val b: Int, val c: Int) {
		fun isValid(): Boolean = listOf(a, b, c).sorted().let { it[0] + it[1] > it[2] }
	}
}