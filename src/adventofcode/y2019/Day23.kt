package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram

fun main() = Day23.solve()

object Day23 : AdventSolution(2019, 23, "Category Six") {

    override fun solvePartOne(input: String): Any? {
        val network = (0..49L).associateWith { IntCodeProgram.fromData(input).apply { input(it) } }

        while (true) {
            network.values.forEach { p ->

                //RR scheduler
                repeat(100) {
                    p.executeStep()
                    if (p.state == IntCodeProgram.State.WaitingForInput) {
                        p.input(-1)
                        p.input(-1)
                        p.executeStep()
                    }
                }

                while (p.outputSize() >= 3) {
                    val destination = p.output()
                    if (destination == 255L) return p.output() to p.output()
                    network[destination]?.let { other ->
                        other.input(p.output()!!)
                        other.input(p.output()!!)
                    }
                }
            }
        }
    }

    override fun solvePartTwo(input: String): Any? {
        val network = (0..49).map { IntCodeProgram.fromData(input).apply { input(it.toLong()) } }

        val idle = BooleanArray(50)

        var lastPacket: Pair<Long, Long> = -1L to -1L
        val sentByNat = mutableSetOf<Pair<Long, Long>>()

        while (true) {
            if (idle.all { it }) {
                idle[0] = false
                if (!sentByNat.add(lastPacket)) {
                    return lastPacket.second
                }
                network[0].input(lastPacket.first)
                network[0].input(lastPacket.second)
            }
            network.forEachIndexed { address, p ->

                //RR scheduler
                repeat(100) {
                    p.executeStep()
                    if (p.state == IntCodeProgram.State.WaitingForInput) {
                        idle[address] = true
                        p.input(-1)
                        p.input(-1)
                        p.executeStep()
                    }
                }

                while (p.outputSize() >= 3) {
                    idle[address] = false
                    val destination = p.output()!!.toInt()
                    if (destination == 255) {
                        lastPacket = p.output()!! to p.output()!!
                    } else {
                        idle[destination] = false
                        network[destination].let { other ->
                            other.input(p.output()!!)
                            other.input(p.output()!!)
                        }
                    }
                }
            }
        }
    }
}
