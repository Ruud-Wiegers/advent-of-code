package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.cartesian

fun main() = Day02.solve()

object Day02 : AdventSolution(2019, 2, "1202 Program Alarm") {

    override fun solvePartOne(input: String) = parse(input).runProgram(12, 2)

    override fun solvePartTwo(input: String): Int? {
        val data = parse(input)

        return (0..99).cartesian()
                .find { (n, v) -> data.runProgram(n, v) == 19690720 }
                ?.let { (n, v) -> 100 * n + v }
    }

    private fun parse(input: String): List<Int> = input.split(',').map(String::toInt)

    private fun List<Int>.runProgram(n: Int, v: Int): Int = toIntArray()
            .also { it[1] = n }
            .also { it[2] = v }
            .let(::IntProgram)
            .run()
            .mem[0]
}

class IntProgram(val mem: IntArray) {
    private var pc: Int = 0

    fun run(): IntProgram {
        while (true) {
            when (mem[pc]) {
                1 -> addI()
                2 -> mulI()
                99 -> return this
                else -> throw IllegalStateException("mem[$pc] == ${mem[pc]}. Not a valid opcode.")
            }
        }
    }

    private val addI = { setA(3, getA(2) + getA(1)); pc += 4 }
    private val mulI = { setA(3, getA(2) * getA(1)); pc += 4 }

    private fun getA(i: Int) = mem[mem[pc + i]]
    private fun setA(i: Int, v: Int) {
        mem[mem[pc + i]] = v
    }
}
