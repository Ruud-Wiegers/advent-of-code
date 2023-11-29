package adventofcode.y2015

import adventofcode.io.AdventSolution

object Day07 : AdventSolution(2015, 7, "Some Assembly Required") {

	override fun solvePartOne(input: String) = WireMap(input.lines()).evaluate("a").toString()

	override fun solvePartTwo(input: String): String {
		val result = WireMap(input.lines()).evaluate("a").toString()
		val amendedInput = "$input\n$result -> b"
		return solvePartOne(amendedInput)
	}
}

private class WireMap(instructions: List<String>) {
	private val wireValueMap: MutableMap<String, Int> = mutableMapOf()
	private val wireConnectionMap: Map<String, () -> Int> = instructions.associate(this::parseInstruction)

	private fun parseInstruction(string: String) = string.split(" ").let { cmd ->
		when (cmd[1]) {
			"AND" -> cmd[4] to { evaluate(cmd[0]) and evaluate(cmd[2]) }
			"OR" -> cmd[4] to { evaluate(cmd[0]) or evaluate(cmd[2]) }
			"LSHIFT" -> cmd[4] to { evaluate(cmd[0]) shl evaluate(cmd[2]) }
			"RSHIFT" -> cmd[4] to { evaluate(cmd[0]) ushr evaluate(cmd[2]) }
			"->" -> cmd[2] to { evaluate(cmd[0]) }
			else -> cmd[3] to { evaluate(cmd[1]).inv() }
		}
	}

	fun evaluate(wire: String): Int = wire.toIntOrNull() ?: wireValueMap.getOrPut(wire) { wireConnectionMap.getValue(wire)() }

}
