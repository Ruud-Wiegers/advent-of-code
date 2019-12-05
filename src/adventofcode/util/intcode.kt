package adventofcode.util


class IntProgram(val mem: IntArray) {
    private var pc: Int = 0
    val inputChannel = mutableListOf<Int>()
    val outputChannel = mutableListOf<Int>()

    fun run(): IntProgram {
        while (true) {
            val op = mem[pc]

            when (op % 100) {
                1    -> add(op)
                2    -> mul(op)
                3    -> input()
                4    -> output(op)
                5    -> jnz(op)
                6    -> jz(op)
                7    -> less(op)
                8    -> eq(op)
                99   -> return this
                else -> throw IllegalStateException("mem[$pc] == ${mem[pc]}. Not a valid opcode.")
            }
        }
    }

    private fun add(modes: Int) {
        set(3, get(modes, 1) + get(modes, 2))
        pc += 4
    }

    private fun mul(modes: Int) {
        set(3, get(modes, 1) * get(modes, 2))
        pc += 4
    }

    private fun input() {
        set(1, inputChannel.removeAt(inputChannel.lastIndex))
        pc += 2
    }

    private fun output(modes: Int) {
        outputChannel += get(modes, 1)
        pc += 2
    }

    private fun jz(modes: Int) {
        pc = if (get(modes, 1) == 0) get(modes, 2)
        else pc + 3
    }

    private fun jnz(modes: Int) {
        pc = if (get(modes, 1) != 0) get(modes, 2)
        else pc + 3
    }

    private fun less(modes: Int) {
        set(3, if (get(modes, 1) < get(modes, 2)) 1 else 0)
        pc += 4
    }

    private fun eq(modes: Int) {
        set(3, if (get(modes, 1) == get(modes, 2)) 1 else 0)
        pc += 4
    }


    private fun isPositionalMode(op: Int, offset: Int): Boolean {
        var mode = op
        repeat(offset + 1) { mode /= 10 }
        return mode and 1 == 0
    }


    private fun get(mode: Int, offset: Int) = if (isPositionalMode(mode, offset))
        mem[mem[pc + offset]]
    else
        mem[pc + offset]

    private fun set(offset: Int, value: Int) {
        mem[mem[pc + offset]] = value
    }
}
