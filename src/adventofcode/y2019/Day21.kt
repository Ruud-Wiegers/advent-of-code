package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram

fun main() = Day21.solve()

object Day21 : AdventSolution(2019, 21, "Springdroid") {

    override fun solvePartOne(input: String): Long? {
        val baseprogram = IntCodeProgram.fromData(input)

        val springscript = listOf(
                "OR A T",
                "AND B T",
                "AND C T",
                "NOT T J",
                "AND D J",
                "WALK").joinToString("\n", postfix = "\n")

        springscript.forEach { baseprogram.input(it.toLong()) }
        baseprogram.execute()


        return generateSequence { baseprogram.output() }.last()
    }

    private fun video(baseprogram: IntCodeProgram) {
        generateSequence { baseprogram.output() }.map { it.toInt().toChar() }.joinToString("").let(::println)
    }


    override fun solvePartTwo(input: String): Long? {
        val baseprogram = IntCodeProgram.fromData(input)

        val springscript = listOf(
                "OR A T",
                "AND B T",
                "AND C T",
                "NOT T J",
                "AND D J",
                "OR H T",
                "OR E T",
                "AND T J",
                "RUN").joinToString("\n", postfix = "\n")

        springscript.forEach { baseprogram.input(it.toLong()) }
        baseprogram.execute()
        return generateSequence { baseprogram.output() }.last()
    }
}