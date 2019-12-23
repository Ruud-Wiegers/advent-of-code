package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram
import adventofcode.util.IntCodeProgram.State.WaitingForInput

fun main() = Day23.solve()

object Day23 : AdventSolution(2019, 23, "Category Six") {

    override fun solvePartOne(input: String): Any? {
        runNetwork(input,
                natReceive = { return@solvePartOne it.y },
                natSend = { return@solvePartOne "Failed" })
    }

    override fun solvePartTwo(input: String): Any? {
        lateinit var lastPacket: Packet
        val sentByNat = mutableSetOf<Packet>()

        runNetwork(input,
                natReceive = { lastPacket = it },
                natSend = {
                    if (sentByNat.add(lastPacket)) return@runNetwork lastPacket
                    else return@solvePartTwo lastPacket.y
                })
    }

    private inline fun runNetwork(
            input: String,
            natReceive: (Packet) -> Unit,
            natSend: () -> Packet
    ): Nothing {
        val network = (0..49).map { NIC(input, it) }

        while (true) {
            if (network.none(NIC::isActive))
                network[0].receive(natSend())

            network.asSequence()
                    .filter(NIC::isActive)
                    .mapNotNull(NIC::step)
                    .forEach {
                        if (it.dest == 255) natReceive(it)
                        else network[it.dest].receive(it)
                    }
        }
    }

    private data class Packet(val dest: Int, val x: Long, val y: Long)

    private class NIC(input: String, val id: Int) {
        private val program = IntCodeProgram.fromData(input).apply { input(id.toLong()) }
        private var missedInputs: Int = 0

        fun isActive() = missedInputs < 2

        fun receive(packet: Packet) {
            missedInputs = if (packet.x < 0 && packet.y < 0) missedInputs + 1 else 0
            program.input(packet.x)
            program.input(packet.y)
        }

        fun step(): Packet? {
            program.executeStep()
            return when {
                program.state == WaitingForInput -> Packet(id, -1, -1)
                program.outputSize() == 3        -> Packet(program.output()!!.toInt(), program.output()!!, program.output()!!)
                else                             -> null
            }
        }
    }
}
