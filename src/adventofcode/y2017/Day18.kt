package adventofcode.y2017

import adventofcode.AdventSolution

object Day18 : AdventSolution(2017, 18, "Duet") {

	override fun solvePartOne(input: String): String {
		val instructions = parseInstructions(input)
		val context = ExecutionContext()
		var ip = 0
		var mostRecentSound = "nothing"

		while (ip in instructions.indices) {
			val (operator, x, y) = instructions[ip]
			when (operator) {
				"set" -> context[x] = context[y]
				"add" -> context[x] += context[y]
				"mul" -> context[x] *= context[y]
				"mod" -> context[x] %= context[y]
				"snd" -> mostRecentSound = context[x].toString()
				"rcv" -> if (context[x] != 0L) return mostRecentSound
				"jgz" -> if (context[x] > 0L) ip += context[y].toInt() - 1
			}
			ip++
		}
		return mostRecentSound
	}

	override fun solvePartTwo(input: String): String {
		val instructions = parseInstructions(input)
		val q1 = mutableListOf<Long>()
		val q2 = mutableListOf<Long>()

		val ec0 = ProgramTwo(0, q1, q2)
		val ec1 = ProgramTwo(1, q2, q1)

		while (ec0.canExecute(instructions) || ec1.canExecute(instructions)) {
			ec0.run(instructions)
			ec1.run(instructions)
		}

		return ec1.sendCount.toString()
	}

	private fun parseInstructions(input: String) = input.split("\n")
			.map { row -> row.split(" ") + "" }
}

private class ProgramTwo(id: Long,
						 private val sendQueue: MutableList<Long>,
						 private val receiveQueue: MutableList<Long>) {

	private val context = ExecutionContext().apply { this["p"] = id }

	private var ip = 0

	var sendCount = 0
		private set

	fun canExecute(instructions: List<List<String>>) = ip in instructions.indices
			&& (instructions[ip][0] != "rcv" || receiveQueue.isNotEmpty())

	fun run(instructions: List<List<String>>) {
		while (canExecute(instructions)) {
			executeNext(instructions[ip])
		}
	}

	private fun executeNext(instruction: List<String>) {
		val (operator, x, y) = instruction
		when (operator) {
			"snd" -> {
				sendQueue += context[x]
				sendCount++
			}
			"rcv" -> context[x] = receiveQueue.removeAt(0)
			"set" -> context[x] = context[y]
			"add" -> context[x] += context[y]
			"mul" -> context[x] *= context[y]
			"mod" -> context[x] %= context[y]
			"jgz" -> if (context[x] > 0L) ip += context[y].toInt() - 1
		}
		ip++
	}
}

class ExecutionContext {
	private val registers: MutableMap<String, Long> = mutableMapOf()

	operator fun get(value: String) = value.toLongOrNull() ?: registers[value] ?: 0

	operator fun set(name: String, value: Long) = registers.put(name, value)
}
