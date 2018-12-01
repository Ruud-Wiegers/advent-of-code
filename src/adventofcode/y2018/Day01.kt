package adventofcode.y2018

import adventofcode.AdventSolution

object Day01 : AdventSolution(2018, 1, "Chronal Calibration") {

	override fun solvePartOne(input: String) =
			input.splitToSequence("\n")
					.map(String::toInt)
					.sum()
					.toString()

	override fun solvePartTwo(input: String): String {
		val deltas = input.splitToSequence("\n").map(String::toInt)

		val frequencies = generateSequence { deltas }.flatten().scan(0, Int::plus)

		val seen = mutableSetOf<Int>()
		frequencies.forEach {
			if (it in seen) return it.toString()
			seen += it
		}
		return "No solution."
	}
}

private fun <T, R> Sequence<T>.scan(initial: R, operation: (R, T) -> R): Sequence<R> {
	var result: R = initial
	return this.map {
		result = operation(result, it)
		result
	}
}
