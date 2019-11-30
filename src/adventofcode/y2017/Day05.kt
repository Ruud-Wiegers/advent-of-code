package adventofcode.y2017

import adventofcode.AdventSolution

object Day05 : AdventSolution(2017, 5, "A Maze of Twisty Trampolines, All Alike") {

	override fun solvePartOne(input: String): String {
		val jumpmap = parse(input)
		val modification: (Int) -> Int = { it + 1 }
		return countJumps(jumpmap, modification)
				.toString()
	}

	override fun solvePartTwo(input: String): String {
		val jumpmap = parse(input)
		val modification: (Int) -> Int = { if (it < 3) it + 1 else it - 1 }
		return countJumps(jumpmap, modification)
				.toString()
	}

	private fun parse(input: String): IntArray = input
			.lines()
			.map { it.toInt() }
			.toIntArray()


	private inline fun countJumps(jumpmap: IntArray, modification: (Int) -> Int): Int {
		var jumpCount = 0
		var currentPosition = 0

		while (currentPosition in jumpmap.indices) {
			val jumpSize = jumpmap[currentPosition]
			jumpmap[currentPosition] = modification(jumpSize)
			currentPosition += jumpSize
			jumpCount++
		}
		return jumpCount
	}
}
