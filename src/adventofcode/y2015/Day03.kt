package adventofcode.y2015

import adventofcode.AdventSolution
import adventofcode.solve

fun main(args: Array<String>) {
	Day03.solve()
}
object Day03 : AdventSolution(2015, 3, "Perfectly Spherical Houses in a Vacuum") {

	override fun solvePartOne(input: String) = visitEach(input).size.toString()

	override fun solvePartTwo(input: String): String {
		val (s1, s2) = input.withIndex().partition { it.index % 2 == 0 }
		val route1 = s1.map { it.value }.joinToString("")
		val route2 = s2.map { it.value }.joinToString("")

		val visited = visitEach(route1) + visitEach(route2)

		return visited.size.toString()
	}

	private fun visitEach(route: String): MutableSet<Pair<Int, Int>> {
		var x = 0
		var y = 0

		val visited = mutableSetOf(Pair(0 , 0))

		route.forEach {
			when (it) {
				'^' -> y++
				'v' -> y--
				'<' -> x++
				'>' -> x--
			}
			visited += Pair(x, y)
		}
		return visited
	}
}

