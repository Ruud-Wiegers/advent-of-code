package adventofcode.util

class IntCodeProgram(input: List<Int>) {
    private val data = input.withIndex()
            .associate { it.index.toLong() to it.value.toLong() }
            .toMutableMap()
            .let(::Memory)

    var state = State.Ready; private set

    private val inputChannel = mutableListOf<Long>()
    private val outputChannel = mutableListOf<Long>()

    fun input(i: Long) = inputChannel.add(i)

    fun output(): Long? = if (outputChannel.isNotEmpty()) outputChannel.removeAt(0) else null

    fun execute() {
        do {
            data.nextInstruction()
            when (data.currentOperation) {
                1 -> data[2] = data[0] + data[1]
                2 -> data[2] = data[0] * data[1]
                3 -> state = if (inputChannel.isEmpty())
                    State.WaitingForInput
                else
                    State.Ready.also { data[0] = inputChannel.removeAt(0) }
                4 -> outputChannel.add(data[0])
                5 -> if (data[0] != 0L) data.pc = data[1] - 3
                6 -> if (data[0] == 0L) data.pc = data[1] - 3
                7 -> data[2] = if (data[0] < data[1]) 1 else 0
                8 -> data[2] = if (data[0] == data[1]) 1 else 0
                9 -> data.relativeBase += data[0]
                99 -> state = State.Halted
                else -> throw IllegalStateException()
            }
            if (state == State.Ready)
                data.pc += when (data.currentOperation) {
                    3, 4, 9 -> 2
                    5, 6 -> 3
                    1, 2, 7, 8 -> 4
                    else -> throw IllegalStateException()
                }
        } while (state == State.Ready)
    }

    enum class State { Ready, WaitingForInput, Halted }
}

private class Memory(private val memory: MutableMap<Long, Long>) {
    var pc: Long = 0L
    var relativeBase: Long = 0L
    var currentOperation: Int = 0; private set

    private var instruction: Int = 0
    private var modes: List<Mode> = emptyList()
    private var params: List<Long> = emptyList()

    fun nextInstruction() {
        instruction = memory.getValue(pc).toInt()
        currentOperation = instruction % 100
        modes = listOf(instruction / 100 % 10, instruction / 1000 % 10, instruction / 10000 % 10).map { Mode.values()[it] }
        params = (1..3).map { memory[pc + it] ?: 0L }
    }

    operator fun get(offset: Int) = when (modes[offset]) {
        Mode.Direct -> params[offset]
        Mode.Positional -> memory[params[offset]] ?: 0L
        Mode.Relative -> memory[relativeBase + params[offset]] ?: 0L
    }

    operator fun set(offset: Int, value: Long) = when (modes[offset]) {
        Mode.Direct -> throw IllegalStateException("cannot write with direct addressing")
        Mode.Positional -> memory[params[offset]] = value
        Mode.Relative -> memory[relativeBase + params[offset]] = value
    }

    enum class Mode { Positional, Direct, Relative }
}
