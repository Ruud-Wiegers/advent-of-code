package adventofcode.y2016

import adventofcode.language.assembunny.AssemBunnyContext
import adventofcode.language.assembunny.parseToAssemBunny
import adventofcode.io.AdventSolution

object Day12 : AdventSolution(2016, 12, "Leonardo's Monorail") {
	override fun solvePartOne(input: String): String {

		val program = parseToAssemBunny(input).toMutableList()
		val registers = mutableListOf(0, 0, 0, 0)
		val s = AssemBunnyContext(registers, 0, program)
		s.run()
		return s.registers[0].toString()
	}

	override fun solvePartTwo(input: String): String {

		val program = parseToAssemBunny(input).toMutableList()
		val registers = mutableListOf(0, 0, 1, 0)
		val s = AssemBunnyContext(registers, 0, program)
		s.run()
		return s.registers[0].toString()
	}
}
