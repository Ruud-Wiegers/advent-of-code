package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day19.solve()

object Day19 : AdventSolution(2018, 19, "Go With The Flow") {

    override fun solvePartOne(input: String): Int {
        val program = parse(input).toList()

        var registers = listOf(0, 0, 0, 0, 0, 0)
        while (registers[3] in program.indices) {
            val instruction = program[registers[3]]
            registers = allOperations[instruction.opcode].execute(registers, instruction).toMutableList()
            registers[3]++
        }

        return registers[0]
    }

    override fun solvePartTwo(input: String): Int {
        val program = parse(input).toList()

        var registers = listOf(1, 0, 0, 0, 0, 0)
        while (registers[3] in program.indices) {
            val instruction = program[registers[3]]
            registers = allOperations[instruction.opcode].execute(registers, instruction).toMutableList()
            registers[3]++
        }

        return registers[0]
    }


    private fun parse(input: String) = input.splitToSequence('\n')
            .drop(1)
            .map {
                it.split(" ")
                        .let { (a, b, c, d) -> Instruction(ops[a]!!, b.toInt(), c.toInt(), d.toInt()) }
            }
}


private fun Operation.execute(registers: Registers, instruction: Instruction): Registers =
        registers.toMutableList().also { regs ->
            regs[instruction.target] = this(registers, instruction.input1, instruction.input2)
        }

private val allOperations = listOf<Operation>(
        { r, a, b -> r[a] + r[b] },
        { r, a, b -> r[a] + b },
        { r, a, b -> r[a] * r[b] },
        { r, a, b -> r[a] * b },
        { r, a, b -> r[a] and r[b] },
        { r, a, b -> r[a] and b },
        { r, a, b -> r[a] or r[b] },
        { r, a, b -> r[a] or b },
        { r, a, _ -> r[a] },
        { _, a, _ -> a },
        { r, a, b -> if (a > r[b]) 1 else 0 },
        { r, a, b -> if (r[a] > b) 1 else 0 },
        { r, a, b -> if (r[a] > r[b]) 1 else 0 },
        { r, a, b -> if (a == r[b]) 1 else 0 },
        { r, a, b -> if (r[a] == b) 1 else 0 },
        { r, a, b -> if (r[a] == r[b]) 1 else 0 }
)

private val ops = listOf("addr", "addi", "mulr", "muli", "banr", "bani", "borr", "bori", "setr", "seti", "gtir", "gtri", "gtrr", "eqir", "eqri", "eqrr")
        .withIndex().associate { (i, o) -> o to i }