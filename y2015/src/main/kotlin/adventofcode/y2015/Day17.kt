package adventofcode.y2015

import adventofcode.AdventSolution

object Day17 : AdventSolution(2015, 17, "No Such Thing as Too Much") {

	override fun solvePartOne(input: String) = input.lines()
			.map { it.toInt() }
			.let { partitions(it, 150) }
			.toString()

	override fun solvePartTwo(input: String) = input.lines()
			.map { it.toInt() }
			.let { partitions2(it, 0, 150) }
			.groupingBy { it }.eachCount()
			.minByOrNull { (k, _) -> k }
			?.value
			.toString()

	private fun partitions(parts: List<Int>, total: Int): Int = when {
		total == 0 -> 1
		total < 0 -> 0
		parts.isEmpty() -> 0
		else -> partitions(parts.drop(1), total) +
				partitions(parts.drop(1), total - parts[0])

	}

	private fun partitions2(parts: List<Int>, used: Int, total: Int): List<Int> = when {
		total == 0 -> listOf(used)
		total < 0 -> emptyList()
		parts.isEmpty() -> emptyList()
		else -> partitions2(parts.drop(1), used, total) +
				partitions2(parts.drop(1), used + 1, total - parts[0])

	}
}
