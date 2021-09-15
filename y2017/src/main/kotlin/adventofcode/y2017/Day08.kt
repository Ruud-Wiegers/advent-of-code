package adventofcode.y2017

import adventofcode.AdventSolution

object Day08 : AdventSolution(2017, 8, "I Heard You Like Registers") {

	override fun solvePartOne(input: String): String {
		val registers = mutableMapOf<String, Int>()

		for (instruction in parseInput(input)) {
			instruction.executeOn(registers)
		}

		return registers.values.maxOrNull().toString()
	}

	override fun solvePartTwo(input: String): String {
		val registers = mutableMapOf<String, Int>()
		return parseInput(input)
			.onEach { it.executeOn(registers) }
			.maxOf { registers[it.register] ?: 0 }
			.toString()
	}


	private fun parseInput(input: String): Sequence<Instruction> {
		val regex = "([a-z]+) ([a-z]+) (-?\\d+) if ([a-z]+) ([^ ]+) (-?\\d+)"
				.toRegex()
		return input.lineSequence()
				.map { regex.matchEntire(it)?.destructured!! }
				.map { (a, b, c, d, e, f) ->
					val sign = if (b == "inc") 1 else -1
					Instruction(a, c.toInt() * sign, d, e, f.toInt())
				}
	}

	private data class Instruction(val register: String,
								   private val amount: Int,
								   private val comparandRegister: String,
								   private val comparator: String,
								   private val comparand: Int) {
		fun executeOn(registers: MutableMap<String, Int>) {
			if (compare(registers[comparandRegister] ?: 0)) {
				registers[register] = (registers[register] ?: 0) + amount
			}
		}

		fun compare(v: Int) = when (comparator) {
			"<" -> v < comparand
			"==" -> v == comparand
			">" -> v > comparand
			"<=" -> v <= comparand
			">=" -> v >= comparand
			"!=" -> v != comparand
			else -> throw IllegalStateException(comparator)
		}
	}
}
