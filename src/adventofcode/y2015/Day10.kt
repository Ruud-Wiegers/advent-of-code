package adventofcode.y2015

import adventofcode.AdventSolution


object Day10 : AdventSolution(2015, 10, "Elves Look, Elves Say") {

	override fun solvePartOne(input: String): String {
		var res = input
		repeat(40) { res = looksay(res) }
		return res.length.toString()
	}

	override fun solvePartTwo(input: String): String {
		var res = input
		repeat(50) { res = looksay(res) }
		return res.length.toString()
	}


	private fun looksay(s: String): String {
		val out = StringBuilder()
		var i = 0
		while (i < s.length) {
			var count = 1
			val c = s[i]
			while (i + 1 < s.length && s[i + 1] == c) {
				++i
				++count
			}
			out.append(count)
			out.append(c)
			i++
		}
		return out.toString()
	}

}