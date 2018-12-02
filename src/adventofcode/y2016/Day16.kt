package adventofcode.y2016

import adventofcode.AdventSolution

object Day16 : AdventSolution(2016, 16, "Dragon Checksum ") {
	override fun solvePartOne(input: String) = fillDisk(input, 272).checksum()
	override fun solvePartTwo(input: String) = fillDisk(input, 35651584).checksum()
}

private fun fillDisk(inputValue: String, size: Int): String =
		generateSequence(inputValue, String::dragon)
				.first { it.length >= size }
				.take(size)

private fun String.dragon() = reversed()
		.replace('0', 'x')
		.replace('1', '0')
		.replace('x', '1')
		.let { this + '0' + it }


private fun String.checksum(): String =
		generateSequence(this, String::diff)
				.first { it.length % 2 != 0 }

private fun String.diff(): String = (indices step 2)
		.asSequence()
		.map { this[it] == this[it + 1] }
		.map { if (it) '1' else '0' }
		.fold(StringBuilder(), StringBuilder::append)
		.toString()
