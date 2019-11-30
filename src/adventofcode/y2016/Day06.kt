package adventofcode.y2016

import adventofcode.AdventSolution

private typealias CharacterSelector = (frequencyMap: Map<Char, Int>) -> Map.Entry<Char, Int>?

object Day06 : AdventSolution(2016, 6, "Signals and Noise") {
	override fun solvePartOne(input: String) = solve(input) { it.maxBy { (_, freq) -> freq } }
	override fun solvePartTwo(input: String) = solve(input) { it.minBy { (_, freq) -> freq } }

	private inline fun solve(input: String, crossinline selectCharFromFreqMap: CharacterSelector) = input.lines()
			.transpose()
			.asSequence()
			.map { str -> str.groupingBy { it }.eachCount() }
			.map { selectCharFromFreqMap(it) }
			.map { it!!.key }
			.joinToString("")

	private fun List<String>.transpose(): List<String> =
			first().indices.map { index ->
				asSequence().map { it[index] }.joinToString("")
			}
}
