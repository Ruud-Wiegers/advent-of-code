package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.language.intcode.IntCodeProgram

fun main() = Day21.solve()

object Day21 : AdventSolution(2019, 21, "Springdroid") {

    override fun solvePartOne(input: String) = runDroid(input, listOf(
            "OR A T",
            "AND B T",
            "AND C T",
            "NOT T J",
            "AND D J",
            "WALK"))

    override fun solvePartTwo(input: String) = runDroid(input, listOf(
            "OR A T",
            "AND B T",
            "AND C T",
            "NOT T J",
            "AND D J",
            "OR E T",
            "OR H T",
            "AND T J",
            "RUN"))

    private fun runDroid(input: String, springscript: List<String>): Long {
        val baseprogram = IntCodeProgram.fromData(input)
        springscript.joinToString("\n", postfix = "\n").forEach { baseprogram.input(it.code.toLong()) }
        baseprogram.execute()
        return generateSequence { baseprogram.output() }.last()
    }

}