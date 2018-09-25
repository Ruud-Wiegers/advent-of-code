package adventofcode.y2016

import adventofcode.util.AssemBunnyContext
import adventofcode.util.parseToAssemBunny
import adventofcode.AdventSolution


object Day23 : AdventSolution(2016, 23, "Safe Cracking") {
	override fun solvePartOne(input: String): String {

		val program = parseToAssemBunny(input).toMutableList()
		val registers = mutableListOf(7, 0, 0, 0)
		val s = AssemBunnyContext(registers, 0, program)
		s.run()
		return s.registers[0].toString()
	}

	override fun solvePartTwo(input: String): String {

		val program = parseToAssemBunny(input).toMutableList()
		val registers = mutableListOf(12, 0, 0, 0)
		val s = AssemBunnyContext(registers, 0, program)
		s.run()
		return s.registers[0].toString()
	}
}

