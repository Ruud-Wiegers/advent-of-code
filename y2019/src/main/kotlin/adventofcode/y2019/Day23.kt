package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.language.intcode.IntCodeProgram
import adventofcode.language.intcode.IntCodeProgram.State.WaitingForInput

fun main() = Day23.solve()

object Day23 : AdventSolution(2019, 23, "Category Six") {

    override fun solvePartOne(input: String): Any? {
        runNetwork(input,
                onNatReceive = { return@solvePartOne it.y },
                onNatSend = { throw IllegalStateException() })
    }

    override fun solvePartTwo(input: String): Any? {
        lateinit var lastPacket: Packet
        val sentByNat = mutableSetOf<Packet>()

        runNetwork(input,
                onNatReceive = { lastPacket = it },
                onNatSend = {
                    if (sentByNat.add(lastPacket))
                        lastPacket.copy(destination = 0)
                    else
                        return@solvePartTwo lastPacket.y
                })
    }

    private inline fun runNetwork(
            input: String,
            onNatReceive: (Packet) -> Unit,
            onNatSend: () -> Packet
    ): Nothing {
        val network = (0..49).map { NIC(input, it) }

        while (true) {
            if (network.none(NIC::isActive))
                network[0].receive(onNatSend())

            network.asSequence()
                    .filter(NIC::isActive)
                    .mapNotNull(NIC::step)
                    .forEach {
                        if (it.destination == 255) onNatReceive(it)
                        else network[it.destination].receive(it)
                    }
        }
    }

    private data class Packet(val destination: Int, val x: Long, val y: Long)

    private class NIC(input: String, val id: Int) {
        private val program = IntCodeProgram.fromData(input).apply {
            input(id.toLong())
        }
        private var missedInputs: Int = 0

        fun isActive() = missedInputs < 2

        fun receive(packet: Packet) {
            missedInputs = 0
            program.input(packet.x)
            program.input(packet.y)
        }

        fun step(): Packet? {
            program.executeStep()
            if (program.state == WaitingForInput) {
                missedInputs++
                program.input(-1)
            }
            return if (program.outputSize() == 3) {
                missedInputs = 0
                Packet(program.output()!!.toInt(), program.output()!!, program.output()!!)
            } else null
        }
    }
}
