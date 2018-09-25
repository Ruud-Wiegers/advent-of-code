package adventofcode.y2016

import adventofcode.AdventSolution
import nl.ruudwiegers.adventofcode.util.md5

object Day14 : AdventSolution(2016, 14, "One-Time Pad") {

	override fun solvePartOne(input: String) = generateSequence(0) { it + 1 }
			.map { md5("$input$it") }
			.solve()

	override fun solvePartTwo(input: String) = generateSequence(0) { it + 1 }
			.map { generateSequence("$input$it",::md5).take(2018).last() }
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
			.take(64)
			.last()
			.index
			.toString()


	private fun String.threes(): Char? = windowedSequence(3).firstOrNull { sub -> sub.all { ch -> sub[0] == ch } }?.get(0)

	private fun String.five(expected: Char): Boolean = windowedSequence(5).any { sub -> sub.all { it == expected } }


}
