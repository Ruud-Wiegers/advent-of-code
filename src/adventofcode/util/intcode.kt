package adventofcode.util

import java.nio.channels.spi.AbstractInterruptibleChannel


class IntProgram(val mem: IntArray, var inputChannel: MutableList<Int> = mutableListOf()) {
    private var pc: Int = 0

    var halt = false
    val outputChannel = mutableListOf<Int>()

    fun run(): IntProgram {
        while (!halt) {
            step()
        }
        return this
    }



    fun step() {
        when (mem[pc] % 100) {
            1 -> add()
            2 -> multiply()
            3 -> readInput()
            4 -> writeOutput()
            5 -> jumpNonZero()
            6 -> jumpIfZero()
            7 -> lessThan()
            8 -> equal()
            99 -> halt = true
            else -> throw IllegalStateException("mem[$pc] == ${mem[pc]}. Not a valid opcode.")
        }
    }


    private fun readInput() {
        val value = inputChannel.removeAt(0)
        store(1, value)
        pc += 2
    }

    private fun writeOutput() {
        outputChannel += load(1)
        pc += 2
    }

    private fun jumpIfZero() {
        pc = if (load(1) == 0) load(2)
        else pc + 3
    }

    private fun jumpNonZero() {
        pc = if (load(1) != 0) load(2)
        else pc + 3
    }

    private fun add() = binaryOperation { a, b -> a + b }
    private fun multiply() = binaryOperation { a, b -> a * b }
    private fun lessThan() = binaryOperation { a, b -> if (a < b) 1 else 0 }
    private fun equal() = binaryOperation { a, b -> if (a == b) 1 else 0 }

    private inline fun binaryOperation(operator: (Int, Int) -> Int) {
        val value = operator(load(1), load(2))
        store(3, value)
        pc += 4
    }

    private fun store(offset: Int, value: Int) {
        mem[mem[pc + offset]] = value
    }

    private fun load(offset: Int) = if (isPositionalMode(mem[pc], offset))
        mem[mem[pc + offset]]
    else
        mem[pc + offset]

    private fun isPositionalMode(op: Int, offset: Int): Boolean {
        var mode = op
        repeat(offset + 1) { mode /= 10 }
        return mode and 1 == 0
    }
}
