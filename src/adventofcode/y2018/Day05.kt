package adventofcode.y2018

import adventofcode.AdventSolution
import java.util.*

object Day05 : AdventSolution(2018, 5, "Alchemical Reduction") {

	override fun solvePartOne(input: String): String {
		val processed = process(input)
		return processed.toString()
	}

	override fun solvePartTwo(input: String): String {
		return ('a'..'z')
				.asSequence()
				.map { ch -> input.filter { it.toLowerCase() != ch } }
				.map { process(it) }
				.min()
				.toString()
	}

	private fun process(input: String): Int {
		val processed = Stack<Char>()
		for (ch in input) {
			if (processed.isEmpty() || ch == processed.peek() || ch.toLowerCase() != processed.peek().toLowerCase())
				processed.push(ch)
			else
				processed.pop()
		}
		return processed.size
	}

}
