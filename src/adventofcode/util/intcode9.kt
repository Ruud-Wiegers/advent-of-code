package adventofcode.util

class IntProgram9(mem: List<Int>) {
    private var pc: Long = 0L
    private var relativeBase: Long = 0L

    private val mem = mem.withIndex().associate { it.index.toLong() to it.value.toLong() }.toMutableMap()

    var state = State.Ready; private set
    private val inputChannel = mutableListOf<Long>()
    private val outputChannel = mutableListOf<Long>()


    fun input(i: Long) {
        inputChannel += i
    }

    fun output() = outputChannel.takeIf { it.isNotEmpty() }?.removeAt(0)

    fun execute() {
        do {
            step()
        } while (state == State.Ready)
    }


    private fun step() {
        val op = (loadImmediate(0).toInt())
        when (op % 100) {
            1    -> add()
            2    -> multiply()
            3    -> readInput()
            4    -> writeOutput()
            5    -> jumpNonZero()
            6    -> jumpIfZero()
            7    -> lessThan()
            8    -> equal()
            9    -> rebase()
            99   -> state = State.Halted
            else -> throw IllegalStateException("mem[$pc] == ${mem[pc]}. Not a valid opcode.")
        }
    }


    private fun readInput() {
        state = if (inputChannel.isEmpty()) State.WaitingForInput else {
            val value = inputChannel.removeAt(0)
            store(1, value)
            pc += 2
            State.Ready
        }
    }

    private fun writeOutput() {
        outputChannel += load(1)
        pc += 2
    }

    private fun jumpIfZero() {
        pc = if (load(1) == 0L) load(2)
        else pc + 3
    }

    private fun jumpNonZero() {
        pc = if (load(1) != 0L) load(2)
        else pc + 3
    }

    private fun rebase() {
        relativeBase += load(1)
        pc += 2
    }

    private fun add() = binaryOperation { a, b -> a + b }
    private fun multiply() = binaryOperation { a, b -> a * b }
    private fun lessThan() = binaryOperation { a, b -> if (a < b) 1 else 0 }
    private fun equal() = binaryOperation { a, b -> if (a == b) 1 else 0 }

    private inline fun binaryOperation(operator: (Long, Long) -> Long) {
        val value = operator(load(1), load(2))
        store(3, value)
        pc += 4
    }

    private fun store(offset: Int, value: Long) {
        when (mode(loadImmediate(0), offset)) {
            0    -> mem[loadImmediate(offset)] = value
            2    -> mem[relativeBase + loadImmediate(offset)] = value
            else -> throw IllegalStateException("unknown addressing mode: $value")
        }
    }

    private fun load(offset: Int) = when (mode(loadImmediate(0), offset)) {
        0    -> mem[loadImmediate(offset)] ?: 0L
        1    -> loadImmediate(offset)
        2    -> mem[relativeBase + loadImmediate(offset)] ?: 0L
        else -> throw IllegalStateException("unknown addressing mode")
    }

    private fun loadImmediate( offset: Int) = mem[pc + offset] ?: 0L

    private fun mode(op: Long, offset: Int): Int {
        var mode = op
        repeat(offset + 1) { mode /= 10 }
        return (mode % 10).toInt()
    }

    enum class State { Ready, WaitingForInput, Halted }
}
