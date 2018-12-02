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

		for (box in boxes)
			for (other in boxes) {
				val matchingCharacters = box.filterIndexed { i, ch -> other[i] == ch }
				if (matchingCharacters.length == box.length - 1) return matchingCharacters
			}
		return ""
	}
}
