package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.cartesian

fun main() = Day02.solve()

object Day02 : AdventSolution(2019, 2, "1202 Program Alarm") {

    override fun solvePartOne(input: String) = parse(input)
            .init(12, 2)
            .toIntProgram()
            .run()
            .mem[0]

    override fun solvePartTwo(input: String): String? {
        val p = parse(input)
        fun display(n: Int) = n.toString().padStart(2, '0')
        return (0..99).cartesian()
                .find { (n, v) -> p.init(n, v).toIntProgram().run().mem[0] == 19690720 }
                ?.let { (n, v) -> display(n) + display(v) }
    }

    private fun parse(input: String) = input.split(",").map(String::toInt).toIntArray()

    private fun IntArray.init(noun: Int, verb: Int) = clone().apply {
        this[1] = noun
        this[2] = verb
    }


}


 fun IntArray.toIntProgram() = IntProgram(this)
 class IntProgram(val mem: IntArray, private var pc: Int = 0) {
    fun run(): IntProgram {
        while (pc >= 0) {
            when (mem[pc]) {
                1 -> addI()
                2 -> mulI()
                99 -> halt()
                else -> throw IllegalStateException("mem[$pc] == ${mem[pc]}. Not a valid opcode.")
            }
        }
        return this
    }

    private val addI = { setI(3, getI(2) + getI(1));pc += 4 }
    private val mulI = { setI(3, getI(2) * getI(1));pc += 4 }
    private val halt = { pc = -1 }

    private fun getI(i: Int) = mem[mem[pc + i]]
    private fun setI(i: Int, v: Int) {
        mem[mem[pc + i]] = v
    }
 }
