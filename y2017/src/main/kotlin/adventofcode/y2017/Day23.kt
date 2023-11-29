package adventofcode.y2017

import adventofcode.io.AdventSolution

object Day23 : AdventSolution(2017, 23, "Coprocessor Conflagration") {

	override fun solvePartOne(input: String): String {
		val instructions = parseInstructions(input)
		val context = ExecutionContext()
		var ip = 0
		var mulCount = 0

		while (ip in instructions.indices) {
			val (operator, x, y) = instructions[ip]
			when (operator) {
				"set" -> context[x] = context[y]
				"sub" -> context[x] -= context[y]
				"mul" -> {
					context[x] *= context[y]
					mulCount++
				}
				"jnz" -> if (context[x] != 0L) ip += context[y].toInt() - 1
			}
			ip++
		}
		return mulCount.toString()
	}

	override fun solvePartTwo(input: String): String = (106700..123700 step 17)
			.count(this::isComposite)
			.toString()

	private fun isComposite(n: Int) = (2 until n).any { n % it == 0 }

	private fun parseInstructions(input: String) = input.lines()
			.map { row -> row.split(" ") + "" }
}

