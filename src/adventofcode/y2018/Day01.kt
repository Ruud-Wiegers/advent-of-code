package adventofcode.y2018

import adventofcode.AdventSolution

object Day01 : AdventSolution(2018, 1, "Chronal Calibration") {

	override fun solvePartOne(input: String) =
			input.splitToSequence("\n")
					.map(String::toInt)
					.sum()
					.toString()

	override fun solvePartTwo(input: String): String {
		val deltas = input.splitToSequence("\n")
				.map(String::toInt)
				.toList()

		return generateSequence { deltas }
				.flatten()
				.scan(0, Int::plus)
				.findFirstRepetition()
				.toString()
	}
}

private fun <T, R> Sequence<T>.scan(initial: R, operation: (R, T) -> R): Sequence<R> {
	var result: R = initial
	return this.map {
		result = operation(result, it)
		result
	}
}

private fun <T> Sequence<T>.findFirstRepetition(): T? {
	val seen = mutableSetOf<T>()
	forEach {
		if (it in seen) return it
		seen += it
	}
	return null
}
