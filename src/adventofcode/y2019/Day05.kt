package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day05.solve()

object Day05 : AdventSolution(2019, 5, "Secure Container") {

    override fun solvePartOne(input: String) = parse(input).toIntArray().let(::IntProgram5).run()


    override fun solvePartTwo(input: String): Int? {
        return 0
    }

    private fun parse(input: String): List<Int> = input.split(',').map(String::toInt)
}

class IntProgram5(val mem: IntArray) {
    private var pc: Int = 0

    fun run(): IntProgram5 {
        while (true) {
            val op = mem[pc]


            when (op % 100) {
                1 -> add(op / 100)
                2 -> mul(op / 100)
                3 -> input()
                4 -> output(op / 100)
                5 -> jnz(op / 100)
                6 -> jz(op / 100)
                7 -> less(op / 100)
                8 -> eq(op / 100)
                99 -> return this
                else -> throw IllegalStateException("mem[$pc] == ${mem[pc]}. Not a valid opcode.")
            }
        }
    }

    private fun add(modes: Int) {
        set(3, get(modes and 10, 2) + get(modes and 1, 1)); pc += 4
    }

    private fun mul(modes: Int) {
        set(3, get(modes and 10, 2) * get(modes and 1, 1)); pc += 4
    }

    private fun input() {
        set(1, 5); pc += 2
    }

    private fun output(modes: Int) {
        println(get(modes and 1, 1))
        pc += 2
    }

    private fun jz(modes: Int) {
        if (get(modes and 1, 1) == 0) pc = get(modes and 10, 2)
        else pc += 3
    }

    private fun jnz(modes: Int) {
        if (get(modes and 1, 1) != 0) pc = get(modes and 10, 2)
        else pc += 3
    }

    private fun less(modes: Int) {
        val cmp = get(modes and 1, 1) < get(modes and 10, 2)
        set(3, if (cmp) 1 else 0); pc += 4
    }

    private fun eq(modes: Int) {
        val cmp = get(modes and 1, 1) == get(modes and 10, 2)
        set(3, if (cmp) 1 else 0); pc += 4
    }


    private fun get(mode: Int, offset: Int) = if (mode == 0) getA(offset) else getI(offset)
    private fun set(offset: Int, value: Int) = setA(offset, value)

    private fun getA(i: Int) = mem[mem[pc + i]]
    private fun setA(i: Int, v: Int) {
        mem[mem[pc + i]] = v
    }

    private fun getI(i: Int) = mem[pc + i]
}

