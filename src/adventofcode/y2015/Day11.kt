package adventofcode.y2015

import adventofcode.AdventSolution


object Day11 : AdventSolution(2015, 11, "Corporate Policy") {

	override fun solvePartOne(input: String) = nextPwd(input)
	override fun solvePartTwo(input: String) = nextPwd(nextPwd(input))

	private fun nextPwd(str: String) = generateSequence(next(str)) { next(it) }
			.filter { increasing(it) }
			.filter { doubles(it) }
			.first()

	private fun next(str: String): String = when (str.last()) {
		'h' -> str.dropLast(1) + 'j'
		'k' -> str.dropLast(1) + 'm'
		'n' -> str.dropLast(1) + 'p'
		'z' -> next(str.dropLast(1)) + 'a'
		else -> str.dropLast(1) + str.last().inc()
	}

	private fun increasing(input: String) = input.windowed(3).any { it[0] + 1 == it[1] && it[1] + 1 == it[2] }
	private fun doubles(input: String) = input.zipWithNext().filter { (a, b) -> a == b }.toSet().size > 1


}