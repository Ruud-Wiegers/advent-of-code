package adventofcode.y2018

import adventofcode.AdventSolution

object Day02 : AdventSolution(2018, 2, "?") {

	override fun solvePartOne(input: String): String {
		val list = input.splitToSequence("\n")
				.map { it.groupingBy { it }.eachCount() }
				.toList()

		val two = list.count { 2 in it.values }
		val three = list.count { 3 in it.values }

		return (two * three).toString()
	}

	override fun solvePartTwo(input: String): String {
		val ids = input.split("\n")
		for (a in ids) {
			for (b in ids) {
				val diff = a.zip(b) { c, d -> if (c == d) c else null }
						.filterNotNull()
						.toCharArray()
						.let { String(it) }
				if (diff.length == a.length - 1)
					return diff
			}
		}

		return ""
	}
}
