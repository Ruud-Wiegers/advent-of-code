package adventofcode.y2016

import adventofcode.AdventSolution

object Day09 : AdventSolution(2016, 9, "Explosives in Cyberspace") {

	private val rule = """(.*?)\((\d+)x(\d+)\)(.*)""".toRegex()

	override fun solvePartOne(input: String): Int {
		val (p, b, r, u) = rule.matchEntire(input)?.destructured ?: return input.length
		return p.length + r.toInt() * b.toInt() + solvePartOne(u.drop(b.toInt()))
	}

	override fun solvePartTwo(input: String): Long {
		val (p, b, r, u) = rule.matchEntire(input)?.destructured ?: return input.length.toLong()
		return p.length + r.toInt() * solvePartTwo(u.take(b.toInt())) + solvePartTwo(u.drop(b.toInt()))
	}
}
