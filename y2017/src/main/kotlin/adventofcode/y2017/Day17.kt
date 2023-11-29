package adventofcode.y2017

import adventofcode.io.AdventSolution

object Day17 : AdventSolution(2017, 17, "Spinlock") {

	override fun solvePartOne(input: String): String {
		val step = input.toInt()
		val spinBuffer = mutableListOf(0)
		var pos = 0
		for (i in 1..2017) {
			pos = (pos + step + 1) % i
			spinBuffer.add(pos, i)
		}
		return spinBuffer[pos + 1].toString()
	}

	override fun solvePartTwo(input: String): String {
		val step = input.toInt()
		var firstItem = 0
		var pos = 0
		for (i in 1..50_000_000) {
			pos = (pos + step + 1) % i
			if (pos == 0) firstItem = i
		}
		return firstItem.toString()
	}
}
