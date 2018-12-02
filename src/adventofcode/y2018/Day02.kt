package adventofcode.y2018

import adventofcode.AdventSolution

object Day02 : AdventSolution(2018, 2, "Inventory Management System") {

	override fun solvePartOne(input: String): String {
		val frequencies = input.split("\n")
				.map { id ->
					id.groupingBy { it }
							.eachCount()
							.values
				}

		val two = frequencies.count { 2 in it }
		val three = frequencies.count { 3 in it }

		return (two * three).toString()
	}

	override fun solvePartTwo(input: String): String {
		val boxes = input.split("\n")

		return boxes.indices.asSequence().flatMap { i ->
			(0 until i).asSequence().map { j ->
				boxes[i] to boxes[j]
			}
		}
				.map { (a, b) -> keepSame(a, b) }
				.first { it.length == boxes[0].length - 1 }
	}

	private fun keepSame(a: String, b: String) = a.filterIndexed { index, ch -> b[index] == ch }
}
