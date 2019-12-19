package adventofcode.util

data class IntCodeProgram(
        val data: Memory,
        var state: State,
        private val inputChannel: MutableList<Long>,
        private val outputChannel: MutableList<Long>
) {
    constructor(input: List<Long>) : this(input.withIndex()
            .associate { it.index.toLong() to it.value }
            .toMutableMap()
            .let { Memory(it) }, State.Ready, mutableListOf(), mutableListOf())

    fun input(i: Long) {
        inputChannel.add(i)
    }

    fun output(): Long? = if (outputChannel.isNotEmpty()) outputChannel.removeAt(0) else null

    fun execute() {
        do {
            data.nextInstruction()
            when (data.currentOperation) {
                1    -> data[2] = data[0] + data[1]
                2    -> data[2] = data[0] * data[1]
                3    -> state = if (inputChannel.isEmpty())
                    State.WaitingForInput
                else
                    State.Ready.also { data[0] = inputChannel.removeAt(0) }
                4    -> outputChannel.add(data[0])
                5    -> data.pc = if (data[0] != 0L) data[1] else data.pc + 3
                6    -> data.pc = if (data[0] == 0L) data[1] else data.pc + 3
                7    -> data[2] = if (data[0] < data[1]) 1 else 0
                8    -> data[2] = if (data[0] == data[1]) 1 else 0
                9    -> data.relativeBase += data[0]
                99   -> state = State.Halted
                else -> throw IllegalStateException()
            }
            if (state == State.Ready)
                data.pc += when (data.currentOperation) {
                    1, 2, 7, 8 -> 4
                    3, 4, 9    -> 2
                    5, 6, 99   -> 0
                    else       -> throw IllegalStateException()
                }
        } while (state == State.Ready)
    }

    enum class State { Ready, WaitingForInput, Halted }

    companion object {
        fun fromData(data: String) = data
                .split(',')
                .map(String::toLong)
                .let { IntCodeProgram(it) }

    }
}

data class Memory(val memory: MutableMap<Long, Long>,
                  var pc: Long = 0L,
                  var relativeBase: Long = 0L,
                  var currentOperation: Int = 0
) {
    private var instruction = 0
    private var modes: List<Mode> = emptyList()
    private var params: List<Long> = emptyList()

    fun nextInstruction() {
        instruction = memory.getValue(pc).toInt()
        currentOperation = instruction % 100
        modes = listOf(instruction / 100 % 10, instruction / 1000 % 10, instruction / 10000 % 10).map { Mode.values()[it] }
        params = (1L..3L).map { memory.getOrDefault(pc + it, 0L) }
    }

    operator fun get(offset: Int) = when (modes[offset]) {
        Mode.Direct     -> params[offset]
        Mode.Positional -> memory.getOrDefault(params[offset], 0L)
        Mode.Relative   -> memory.getOrDefault(relativeBase + params[offset], 0L)
    }

    operator fun set(offset: Int, value: Long) = when (modes[offset]) {
        Mode.Direct     -> throw IllegalStateException("cannot write with direct addressing")
        Mode.Positional -> memory[params[offset]] = value
        Mode.Relative   -> memory[relativeBase + params[offset]] = value
    }

    enum class Mode { Positional, Direct, Relative }
}
