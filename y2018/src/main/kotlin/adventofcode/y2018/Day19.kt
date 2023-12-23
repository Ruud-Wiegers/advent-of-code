package adventofcode.y2018

import adventofcode.io.AdventSolution
import adventofcode.language.elfcode.Instruction
import adventofcode.language.elfcode.execute
import adventofcode.language.elfcode.parseToElfcode

fun main() = Day19.solve()

object Day19 : AdventSolution(2018, 19, "Go With The Flow") {

    override fun solvePartOne(input: String): Int {
        val (ip, program) = parseToElfcode(input)

        val registers = IntArray(6)
        runProgram(registers, ip, program)
        return registers[0]
    }

    override fun solvePartTwo(input: String) = fastDivisorSum(initialize(true))


    private fun runProgram(registers: IntArray, ip: Int, program: List<Instruction>) {
        while (registers[ip] in program.indices) {
            val instruction = program[registers[3]]
            registers.execute(instruction)
            registers[ip]++
        }
    }
}

private fun initialize(a: Boolean): Int {

    var r2 = 2

    r2 *= r2
    r2 *= 19
    r2 *= 11
    var r4 = 2
    r4 *= 22
    r4 += 2
    r2 += r4
    if (a) {
        r4 = 27
        r4 *= 28
        r4 += 29
        r4 *= 30
        r4 *= 14
        r4 *= 32
        r2 += r4
    }
    return r2
}

private fun fastDivisorSum(n: Int) = (1..n).asSequence().filter { n % it == 0 }.sum()