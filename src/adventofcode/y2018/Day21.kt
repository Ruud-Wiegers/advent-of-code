package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.elfcode.Instruction
import adventofcode.util.elfcode.execute
import adventofcode.util.elfcode.parseToElfcode
import adventofcode.y2017.takeWhileDistinct

fun main() = Day21.solve()

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
                val instruction = program[registers[2]]
                if (registers[ip] == 28) yield(registers[3])
                registers.execute(instruction)
                registers[ip]++
            }
        }
    }

    private fun runDisassembledProgram() = sequence {
        var r3 = 0L
        while (true) {
            var r4 = (r3 or 0x10000) * 0x100
            r3 = 10373714
            do {
                r4 /= 0x100
                r3 += r4 and 0xff
                r3 = r3 * 65899 and 0xffffff
            } while (r4 > 0xff)
            yield(r3)
        }
    }
}
