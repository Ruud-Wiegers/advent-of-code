package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.util.collections.cartesian

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

    private fun List<Int>.runProgram(n: Int, v: Int): Int {
        val programData = this.toIntArray()
        programData[1] = n
        programData[2] = v

        val p = IntProgram(programData)
        p.run()
        return p.mem[0]
    }
}

private class IntProgram(val mem: IntArray, private var pc: Int = 0) {
    fun run() {
        while (true) {
            when (mem[pc]) {
                1    -> store(3, load(2) + load(1))
                2    -> store(3, load(2) * load(1))
                99   -> return
                else -> throw IllegalStateException()
            }
            pc += 4
        }
    }

    private fun load(offset: Int) = mem[mem[pc + offset]]
    private fun store(offset: Int, v: Int) {
        mem[mem[pc + offset]] = v
    }
}


