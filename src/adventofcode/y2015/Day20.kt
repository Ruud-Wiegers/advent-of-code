package adventofcode.y2015

import adventofcode.AdventSolution

object Day20 : AdventSolution(2015, 20, "Infinite Elves and Infinite Houses") {

	override fun solvePartOne(input: String): String {
		val sigma = input.toInt() / 10
		val arr = IntArray(sigma) { 1 }
		for (i in 2 until sigma) {
			for (j in i until sigma step i) {
				arr[j] += i
			}
		}
		return arr.indexOfFirst { it >= sigma }.toString()
	}

	override fun solvePartTwo(input: String): String {
		val target = input.toInt()
		val arr = IntArray(target / 11)
		for (elf in 1 until target) {
			for (it in elf..(50 * elf).coerceAtMost(arr.lastIndex) step elf) {
				arr[it] += elf * 11
			}
		}
		return arr.indexOfFirst { it >= target }.toString()
	}
}



