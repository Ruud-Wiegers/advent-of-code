package adventofcode.y2015

import adventofcode.AdventSolution

object Day08 : AdventSolution(2015, 8, "Matchsticks") {

	override fun solvePartOne(input: String) = input
			.lines()
			.sumOf { it.length - unescape(it).length }
			.toString()

	override fun solvePartTwo(input: String): String = input
			.lines()
			.sumOf { escape(it).length - it.length }
			.toString()


	private fun unescape(str: String) = str
			.drop(1).dropLast(1)
			.replace(Regex("""\\x\p{XDigit}\p{XDigit}""")) { "#" }
			.replace("""\\""", "\\")
			.replace("""\"""", "\"")

	private fun escape(str: String) = str
			.replace("\\", """\\""")
			.replace("\"", """\"""")
			.let { "\"" + it + "\"" }

}