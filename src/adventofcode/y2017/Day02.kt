package adventofcode.y2017

import adventofcode.AdventSolution

object Day02 : AdventSolution(2017, 2, "Corruption Checksum") {

	override fun solvePartOne(input: String) = solveForChecksum(input) { it.max()!! - it.min()!! }

	override fun solvePartTwo(input: String) = solveForChecksum(input) { row ->
		row.mapNotNull { i ->
			row.find { j -> i != j && i % j == 0 }
					?.let { i / it }
		}.first()
	}

	private fun solveForChecksum(input: String, checksum: (List<Int>) -> Int) = input.split("\n")
			.map { it.split("\t").map(String::toInt) }
			.sumBy(checksum)
			.toString()
}
