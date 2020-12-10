package adventofcode.y2017

import adventofcode.AdventSolution
import adventofcode.util.collections.takeWhileDistinct

object Day06 : AdventSolution(2017, 6, "Memory Reallocation") {

	override fun solvePartOne(input: String): String {
		val state = parse(input)
		return generateSequence(state) { redistribute(it) }
				.takeWhileDistinct()
				.count()
				.toString()
	}

	override fun solvePartTwo(input: String): String {
		val state = parse(input)
		val seen = generateSequence(state) { redistribute(it) }
				.takeWhileDistinct()
				.last()

		val s = generateSequence(seen) { redistribute(it) }
				.drop(1)
				.takeWhile { it != seen }
				.count() + 1

		return s.toString()
	}

	private fun redistribute(state: List<Int>): List<Int> {
		val max = state.maxOrNull()!!
		val i = state.indexOf(max)
		val newState = state.map { it }.toMutableList()
		newState[i] = 0
		((i + 1)..i + max).forEach {
			newState[it % newState.size]++
		}
		return newState
	}

	private fun parse(input: String) = input
			.split("\t")
			.map { it.toInt() }
}


