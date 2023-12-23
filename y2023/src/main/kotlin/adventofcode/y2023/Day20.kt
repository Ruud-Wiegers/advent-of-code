package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.util.math.lcm

fun main() {
    Day20.solve()
}

object Day20 : AdventSolution(2023, 20, "Pulse Propagation") {

    override fun solvePartOne(input: String): Long {
        val modules = parse(input)

        var lowPulses = 0L
        var highPulses = 0L

        val unprocessed = ArrayDeque<Signal>()

        repeat(1000) {
            unprocessed.add(Signal("button", "broadcaster", false))

            while (unprocessed.isNotEmpty()) {
                val signal = unprocessed.removeFirst()
                if (signal.highPulse) highPulses++ else lowPulses++

                unprocessed += modules[signal.target]?.processSignal(signal).orEmpty()
            }
        }

        return lowPulses * highPulses
    }

    override fun solvePartTwo(input: String): Long {
        val modules = parse(input)

        //input analysis shows 4 conjunctions connected to a single conjunction before rx
        //the conjunction will fire when all connected conjunctions are high simultaneously
        //they each have a different, regular period.

        val moduleBeforeSink = modules.values.filterIsInstance<Conjunction>().single { it.outputs == listOf("rx") }
        val periodOfInputModules = mutableMapOf<String, Long>()

        generateSequence(1L, Long::inc)
            .takeWhile { periodOfInputModules.size != moduleBeforeSink.inputs.size }
            .forEach { buttonPress ->
                val unprocessed = ArrayDeque<Signal>()
                unprocessed.add(Signal("button", "broadcaster", false))

                while (unprocessed.isNotEmpty()) {
                    val signal = unprocessed.removeFirst()
                    unprocessed += modules[signal.target]?.processSignal(signal).orEmpty()

                    if (signal.highPulse && signal.target == moduleBeforeSink.name) {
                        periodOfInputModules[signal.source] = buttonPress
                    }
                }

            }

        return periodOfInputModules.values.reduce(::lcm)
    }
}

private fun parse(input: String): Map<String, Module> {
    val modules = input.lines().map { line ->
        val name = line.substringBefore(" ").replace("%", "").replace("&", "")
        val outputs = line.substringAfter("-> ").split(", ")
        when (line[0]) {
            '%' -> Flipflop(name, outputs, false)
            '&' -> Conjunction(name, outputs)
            else -> Broadcaster(name, outputs)
        }
    }

    modules.filterIsInstance<Conjunction>().forEach { conjunction ->
        modules.filter { conjunction.name in it.outputs }.forEach { input ->
            conjunction.connectInput(input.name)
        }
    }
    return modules.associateBy { it.name }
}


private sealed class Module(val name: String, val outputs: List<String>) {
    fun processSignal(inputSignal: Signal) = emitHighPulse(inputSignal)?.let { highPulse ->
        outputs.map { target -> Signal(name, target, highPulse) }
    }.orEmpty()

    protected abstract fun emitHighPulse(inputSignal: Signal): Boolean?

}

private class Broadcaster(name: String, outputs: List<String>) : Module(name, outputs) {
    override fun emitHighPulse(inputSignal: Signal) = inputSignal.highPulse
}

private class Flipflop(name: String, outputs: List<String>, var active: Boolean = false) : Module(name, outputs) {
    override fun emitHighPulse(inputSignal: Signal) = when {
        inputSignal.highPulse -> null
        else -> !active.also { active = !active }
    }
}

private class Conjunction(
    name: String,
    outputs: List<String>,
    val inputs: MutableMap<String, Boolean> = mutableMapOf()
) :
    Module(name, outputs) {
    override fun emitHighPulse(inputSignal: Signal): Boolean {
        inputs[inputSignal.source] = inputSignal.highPulse
        return inputs.any { !it.value }
    }

    fun connectInput(inputModule: String) {
        inputs[inputModule] = false
    }
}

private data class Signal(val source: String, val target: String, val highPulse: Boolean)
