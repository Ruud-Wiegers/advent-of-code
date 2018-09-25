package adventofcode.y2016

import adventofcode.AdventSolution

object Day16 : AdventSolution(2016, 16, "Dragon Checksum ") {
	override fun solvePartOne(input: String) = fillDisk(input, 272).checksum()
	override fun solvePartTwo(input: String) = fillDisk(input, 35651584).checksum()
}

private tailrec fun fillDisk(inputValue: String, size: Int): String =
		if (inputValue.length >= size)
			inputValue.substring(0, size)
		else
			fillDisk(dragon(inputValue), size)

private fun dragon(a: String) = a
		.reversed()
		.replace('0', 'x')
		.replace('1', '0')
		.replace('x', '1')
		.let { a + '0' + it }


private tailrec fun String.checksum(): String =
		if (length % 2 != 0)
			this
		else
			this.diff().checksum()

private fun String.diff(): String = (indices step 2)
		.asSequence()
		.map { this[it] == this[it + 1] }
		.map { if (it) '1' else '0' }
		.fold(StringBuilder(), StringBuilder::append)
		.toString()
