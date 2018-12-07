package adventofcode.y2018

import adventofcode.AdventSolution


object Day07 : AdventSolution(2018, 7, "The Sum of Its Parts") {

	override fun solvePartOne(input: String): String {
		val dependencies = parse(input)
		val open = ('A'..'Z').toSortedSet()
		val res = mutableListOf<Char>()

		while (open.isNotEmpty()) {
			val n = open.first { dependencies[it]?.none { it in open } ?: true }
			open.remove(n)
			res.add(n)
		}

		return res.joinToString("")
	}

	override fun solvePartTwo(input: String): String {
		val dependencies = parse(input)

		val open = ('A'..'Z').toSortedSet()
		val processing = mutableMapOf<Char, Int>()

		var count = 0

		while (open.isNotEmpty()) {
			while (processing.size < 5) {
				val n = open.firstOrNull {
					dependencies[it]?.none {
						it in open || it in processing.keys
					} ?: true
				} ?: break
				open.remove(n)
				processing[n] = 61 + (n - 'A')
			}
			count++
			for (k in processing.keys.toList()) {
				processing[k] = processing[k]!! - 1
				processing.remove(k, 0)
			}
		}

		return (count + (processing.values.max() ?: 0)).toString()
	}

	private fun parse(input: String) = input
			.splitToSequence("\n")
			.map {
				val a = it.substringAfter("Step ")[0]
				val b = it.substringAfter("before step ")[0]
				a to b
			}
			.groupBy({ it.second }, { it.first })
			.toSortedMap()

}
