package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.elfcode.*

fun main() = Day19.solve()

object Day19 : AdventSolution(2018, 19, "Go With The Flow") {

    override fun solvePartOne(input: String): Int {
        val (ip, program) = parseToElfcode(input)

        val registers = IntArray(6)
        runProgram(registers, ip, program)
        return registers[0]
    }

    override fun solvePartTwo(input: String): Int {
        val (ip, program) = parseToElfcode(input)

        val registers = IntArray(6)
        registers[0] = 1
        runProgram(registers, ip, program)
        return registers[0]
    }

    private fun runProgram(registers: IntArray, ip: Int, program: List<Instruction>) {
        while (registers[ip] in program.indices) {
            val instruction = program[registers[3]]
            registers.execute(instruction)
            registers[ip]++
        }
    }
}