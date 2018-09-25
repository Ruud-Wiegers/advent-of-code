package nl.ruudwiegers.adventofcode.y2015

import adventofcode.AdventSolution

object Day01 : AdventSolution(2015, 1, "Not Quite Lisp") {

	override fun solvePartOne(input: String) = input.sumBy { if (it == '(') 1 else -1 }.toString()

	override fun solvePartTwo(input: String) = input.foldIndexed(0) { index, acc, c ->
		when {
			acc < 0 -> acc
			acc == 0 && c == ')' -> -1 - index
			c == '(' -> acc + 1
			else -> acc - 1
		}
	}.let { (-it).toString() }
}
