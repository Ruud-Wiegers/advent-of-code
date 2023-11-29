package adventofcode.y2017

import adventofcode.io.AdventSolution

object Day02 : AdventSolution(2017, 2, "Corruption Checksum") {

	override fun solvePartOne(input: String) = solveForChecksum(input) { it.maxOrNull()!! - it.minOrNull()!! }

	override fun solvePartTwo(input: String) = solveForChecksum(input) { row ->
		row.mapNotNull { i ->
			row.find { j -> i != j && i % j == 0 }
					?.let { i / it }
		}.first()
	}

	private fun solveForChecksum(input: String, checksum: (List<Int>) -> Int) = input.lines()
			.map { it.split("\t").map(String::toInt) }
			.sumOf(checksum)
			.toString()
}
