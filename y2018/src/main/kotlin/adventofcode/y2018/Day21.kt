package adventofcode.y2018

import adventofcode.io.AdventSolution
import adventofcode.util.collections.takeWhileDistinct
import adventofcode.language.elfcode.Instruction
import adventofcode.language.elfcode.execute
import adventofcode.language.elfcode.parseToElfcode

object Day21 : AdventSolution(2018, 21, "Chronal Conversion") {
    override fun solvePartOne(input: String) = parseToElfcode(input)
        .let { (ip, program) -> runProgram(ip, program) }
        .first()

    override fun solvePartTwo(input: String) = runDisassembledProgram()
        .takeWhileDistinct()
        .last()


    private fun runProgram(ip: Int, program: List<Instruction>): Sequence<Int> {
        val registers = IntArray(6)
        return sequence {
            while (registers[ip] in program.indices) {
                val instruction = program[registers[ip]]
                //this is where the program tests if it should halt
                //by comparing r0 to r3.
                //so instead of trying to match r0,
                //we just look at the sequence of values r3 assumes
                if (registers[ip] == 28) yield(registers[3])
                registers.execute(instruction)
                registers[ip]++
            }
        }
    }

    private fun runDisassembledProgram() = sequence {
        var r3 = 0L
        while (true) {
            var r4 = r3 or 0x10000
            r3 = 10373714
            while (r4 > 0) {
                r3 += r4 % 0x100
                r3 *= 65899
                r3 %= 0x1000000
                r4 /= 0x100
            }
            yield(r3)
        }
    }
}

