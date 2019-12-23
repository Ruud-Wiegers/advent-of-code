package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram
import adventofcode.util.IntCodeProgram.State.WaitingForInput

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
                        lastPacket
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
            if (network.none(NIC::active))
                network[0].receive(onNatSend())

            network.asSequence()
                    .filter(NIC::active)
                    .mapNotNull(NIC::step)
                    .forEach {
                        if (it.destination == 255) onNatReceive(it)
                        else network[it.destination].receive(it)
                    }
        }
    }

    private data class Packet(val destination: Int, val x: Long, val y: Long)

    private class NIC(input: String, id: Int) {
        private val program = IntCodeProgram.fromData(input).apply {
            input(id.toLong())
            input(-1)
            input(-1)
        }

        var active: Boolean = true; private set


        fun receive(packet: Packet) {
            active = true
            program.input(packet.x)
            program.input(packet.y)
        }

        fun step(): Packet? {
            program.executeStep()
            if (program.state == WaitingForInput) active = false
            return if (program.outputSize() == 3)
                Packet(program.output()!!.toInt(), program.output()!!, program.output()!!)
            else null
        }
    }
}
