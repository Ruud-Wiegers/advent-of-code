package adventofcode.y2015

import adventofcode.AdventSolution

//solved on paper
object Day23 : AdventSolution(2015, 23, "Opening the Turing Lock") {

	override fun solvePartOne(input: String) = hailstone(9663).toString()

	override fun solvePartTwo(input: String) = hailstone(77671).toString()
}

private fun hailstone(input: Int): Int {
	val seq = generateSequence(input) {
		if (it % 2 == 0)
			it / 2
		else
			it * 3 + 1
	}
	return seq.indexOfFirst { it == 1 }
}


