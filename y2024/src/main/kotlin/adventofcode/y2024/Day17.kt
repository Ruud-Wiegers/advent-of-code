package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() {
    Day17.solve()
}

object Day17 : AdventSolution(2024, 17, "Chronospatial Computer") {

    override fun solvePartOne(input: String): String {
        val (registers, instructions) = input.parseInput()

        val computer = Computer(registers[0].toLong(), registers[1].toLong(), registers[2].toLong())

        while (computer.pc in instructions.indices) {
            computer.step(instructions)
        }

        return computer.output.joinToString(",")
    }

    override fun solvePartTwo(input: String): Long? {
        val (_, instructions) = input.parseInput()

        fun dfs(digitToFix: Int, constructed: Long): Long? = (0..7).asSequence().firstNotNullOfOrNull {
            val newConstructed = (constructed shl 3) + it
            val computer = Computer(a = newConstructed)
            while (computer.output.isEmpty()) {
                computer.step(instructions)
            }

            when {
                computer.output.first() != instructions[digitToFix] -> null
                digitToFix == 0 -> newConstructed
                else -> dfs(digitToFix - 1, newConstructed)
            }
        }

        return dfs(instructions.lastIndex, 0)
    }

    fun String.parseInput(): Pair<List<Int>, List<Int>> {
        val registers = lines().take(3).map { it.substringAfterLast(' ').toInt() }
        val program = substringAfter("Program: ").split(",").map(String::toInt)

        return Pair(registers, program)
    }

}

private data class Computer(
    var a: Long = 0,
    var b: Long = 0,
    var c: Long = 0,
    var pc: Int = 0,
    val output: MutableList<Int> = mutableListOf()
) {

    fun readOperator(program: List<Int>): Operator = Operator.entries[program[pc]]
    fun readLiteral(program: List<Int>): Long = Operand.Literal(program[pc + 1].toLong()).eval(this).toLong()
    fun readCombo(program: List<Int>): Long = Operand.Combo(program[pc + 1].toLong()).eval(this)

    fun step(program: List<Int>) {
        when (readOperator(program)) {
            Operator.ADV -> a = a shr readCombo(program).toInt()
            Operator.BXL -> b = b xor readLiteral(program)
            Operator.BST -> b = readCombo(program) % 8L
            Operator.JNZ -> if (a != 0L) pc = readLiteral(program).toInt() - 2
            Operator.BXC -> b = b xor c
            Operator.OUT -> output += (readCombo(program) % 8L).toInt()
            Operator.BDV -> b = a shr readCombo(program).toInt()
            Operator.CDV -> c = a shr readCombo(program).toInt()
        }
        pc += 2
    }

}

private enum class Operator { ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV }

private sealed class Operand {
    abstract fun eval(c: Computer): Long

    data class Literal(val value: Long) : Operand() {
        override fun eval(c: Computer) = value
    }

    data class Combo(val value: Long) : Operand() {
        override fun eval(c: Computer) = when (value) {
            in 0L..3L -> value
            4L -> c.a
            5L -> c.b
            6L -> c.c
            else -> error("Invalid operand $value")
        }
    }
}
