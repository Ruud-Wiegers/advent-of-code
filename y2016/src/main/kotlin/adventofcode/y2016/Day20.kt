package adventofcode.y2016

import adventofcode.AdventSolution

object Day20 : AdventSolution(2016, 20, "Firewall Rules") {

	override fun solvePartOne(input: String): String {
		val blacklistedIpRanges = coalesceIpRanges(input)
				.first()

		return if (blacklistedIpRanges.first > 0) "0"
		else (blacklistedIpRanges.last + 1).toString()
	}

	override fun solvePartTwo(input: String): String {
		val blacklistedIpCount = coalesceIpRanges(input)
				.map { (it.last - it.first) + 1 }
				.sum()
		return (4294967296L - blacklistedIpCount).toString()
	}
}

private fun coalesceIpRanges(input: String): List<LongRange> {
	return input.lines()
			.map { it.substringBefore('-').toLong()..it.substringAfter('-').toLong() }
			.sortedBy { it.first }
			.fold(listOf(), ::combineOverlapping)
			.sortedBy { it.first }
			.fold(listOf(), ::combineAdjacent)
			.sortedBy { it.first }
}

private tailrec fun combineOverlapping(list: List<LongRange>, range: LongRange): List<LongRange> {
	val toCombine = list.find { range.first in it || range.last in it || it.first in range }
			?: return list.plusElement(range)

	return when {
		range.first in toCombine && range.last in toCombine -> list
		toCombine.first in range && toCombine.last in range -> list
		else -> combineOverlapping(list.minusElement(toCombine), minOf(range.first, toCombine.first)..maxOf(range.last, toCombine.last))
	}

}

private fun combineAdjacent(list: List<LongRange>, range: LongRange): List<LongRange> {
	val toCombine = list.find { it.last + 1 == range.first } ?: return list.plusElement(range)
	return list.minusElement(toCombine).plusElement(toCombine.first..range.last)
}
