package adventofcode.util

import adventofcode.util.IntCodeProgram.State.*
import java.util.*

class IntCodeProgram(input: List<Int>) {

    private val memory = input.withIndex()
            .associate { it.index.toLong() to it.value.toLong() }
            .toMutableMap()
            .let(::Memory)
    var state = Ready; private set
    private val inputChannel: Queue<Long> = LinkedList()
    private val outputChannel: Queue<Long> = LinkedList()

    fun input(i: Long) = inputChannel.offer(i)

    fun output(): Long? = outputChannel.poll()

    fun execute() {
        do {
            memory.nextInstruction()
            when (memory.currentOperation) {
                1 -> memory[2] = memory[0] + memory[1]
                2 -> memory[2] = memory[0] * memory[1]
                3 -> state = if (inputChannel.isEmpty()) WaitingForInput else Ready.also { memory[0] = inputChannel.poll() }
                4 -> outputChannel.offer(memory[0])
                5 -> if (memory[0] != 0L) memory.pc = memory[1] - 3
                6 -> if (memory[0] == 0L) memory.pc = memory[1] - 3
                7 -> memory[2] = if (memory[0] < memory[1]) 1 else 0
                8 -> memory[2] = if (memory[0] == memory[1]) 1 else 0
                9 -> memory.relativeBase += memory[0]
                99 -> state = Halted
                else -> throw IllegalStateException()
            }
            memory.pc += when (memory.currentOperation) {
                99 -> 0
                3, 4, 9 -> 2
                5, 6 -> 3
                1, 2, 7, 8 -> 4
                else -> throw IllegalStateException()
            }
        } while (state == Ready)
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
            Mode.Positional -> memory[params[offset]] ?: 0L
            Mode.Direct -> params[offset]
            Mode.Relative -> memory[relativeBase + params[offset]] ?: 0L
        }

        operator fun set(offset: Int, value: Long) = when (modes[offset]) {
            Mode.Positional -> memory[params[offset]] = value
            Mode.Direct -> throw IllegalStateException("unknown addressing mode: $value")
            Mode.Relative -> memory[relativeBase + params[offset]] = value
        }
    }

    enum class State { Ready, WaitingForInput, Halted }
    enum class Mode { Positional, Direct, Relative }
}
