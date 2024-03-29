package adventofcode.y2016

import adventofcode.io.AdventSolution
import adventofcode.util.algorithm.md5

object Day14 : AdventSolution(2016, 14, "One-Time Pad") {

	override fun solvePartOne(input: String) = generateSequence(0,Int::inc)
			.map { md5("$input$it") }
			.solve()

	override fun solvePartTwo(input: String) = generateSequence(0,Int::inc)
			.map { generateSequence("$input$it",::md5).elementAt(2017) }
			.solve()

	private fun Sequence<String>.solve() = windowed(1001, partialWindows = true)
			.withIndex()
			.filter {
				val ch = it.value[0].threes()
				if (ch != null)
					it.value.subList(1, it.value.lastIndex).any { s -> s.five(ch) }
				else
					false
			}
			.elementAt(63)
			.index


	private fun String.threes(): Char? = windowedSequence(3).firstOrNull { sub -> sub.all { ch -> sub[0] == ch } }?.get(0)

	private fun String.five(expected: Char): Boolean = windowedSequence(5).any { sub -> sub.all { it == expected } }


}
