package adventofcode.y2016

import adventofcode.language.assembunny.AssemBunnyContext
import adventofcode.language.assembunny.Instruction
import adventofcode.language.assembunny.parseToAssemBunny
import adventofcode.io.AdventSolution


object Day25 : AdventSolution(2016, 25, "Clock Signal") {
	override fun solvePartOne(input: String): String {
		val program = parseToAssemBunny(input).toMutableList()
		return (1..10000).asSequence()
				.filter { test(program, it, 100) }
				.filter { test(program, it, 1_000) }
				.filter { test(program, it, 10_000) }
				.filter { test(program, it, 100_000) }
				.first { test(program, it, 1_000_000) }
				.toString()
	}

	private fun test(program: MutableList<Instruction>, a: Int, runLength: Int): Boolean {
		val registers = mutableListOf(a, 0, 0, 0)
		val s = AssemBunnyContext(registers, 0, program)
		s.run(runLength)
		return s.signal.all { it in 0..1 } && s.signal.asSequence().zipWithNext().all { (a, b) -> a != b }
	}

	override fun solvePartTwo(input: String) = "Free star!"

}

