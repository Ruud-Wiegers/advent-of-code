package adventofcode.y2017

import adventofcode.AdventSolution

object Day19 : AdventSolution(2017, 19, "A Series of Tubes") {

	override fun solvePartOne(input: String): String {
		val lines = input.split("\n")
		val pipeRunner = PipeRunner(lines)
		pipeRunner.run()
		return pipeRunner.word
	}

	override fun solvePartTwo(input: String): String {
		val lines = input.split("\n")
		val pipeRunner = PipeRunner(lines)
		pipeRunner.run()
		return pipeRunner.count.toString()
	}
}

private class PipeRunner(private val pipes: List<String>) {

	private var position = Point(pipes[0].indexOf("|"), 0)
	var direction = Point(0, 1)
	var word = ""
	var count = 0

	fun run() {
		do {
			val ch = charAt(position)
			when (ch) {
				'+' -> direction = findNewDirection()
				in 'A'..'Z' -> word += ch
				' ' -> count--
			}
			position += direction
			count++
		} while (ch != ' ')
	}

	private fun findNewDirection(): Point {
		val options = if (direction.x == 0)
			listOf(Point(-1, 0), Point(1, 0))
		else
			listOf(Point(0, -1), Point(0, 1))

		return options.first { charAt(position + it) in "-|" }
	}

	private fun charAt(p: Point) = pipes.getOrNull(p.y)?.getOrNull(p.x) ?: ' '
}

private data class Point(val x: Int, val y: Int) {
	operator fun plus(o: Point) = Point(x + o.x, y + o.y)
}
