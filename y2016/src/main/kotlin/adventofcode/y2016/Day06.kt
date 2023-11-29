package adventofcode.y2016

import adventofcode.io.AdventSolution
import adventofcode.util.algorithm.transposeString

private typealias CharacterSelector = (frequencyMap: Map<Char, Int>) -> Map.Entry<Char, Int>?

object Day06 : AdventSolution(2016, 6, "Signals and Noise") {
	override fun solvePartOne(input: String) = solve(input) { it.maxByOrNull { (_, freq) -> freq } }
	override fun solvePartTwo(input: String) = solve(input) { it.minByOrNull { (_, freq) -> freq } }

	private inline fun solve(input: String, crossinline selectCharFromFreqMap: CharacterSelector) = input.lines()
			.transposeString()
			.asSequence()
			.map { str -> str.groupingBy { it }.eachCount() }
			.map { selectCharFromFreqMap(it) }
			.map { it!!.key }
			.joinToString("")
}
