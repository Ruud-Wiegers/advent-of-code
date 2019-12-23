package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram

fun main() = Day23.solve()

object Day23 : AdventSolution(2019, 23, "Category Six") {

    override fun solvePartOne(input: String): Any? {
        runNetwork(
                input,
                sendToNat = { _, y -> return@solvePartOne y },
                activateNat = { return@solvePartOne "Failed" })
        return "Failed"
    }

    override fun solvePartTwo(input: String): Any? {
        var lastPacket: Pair<Long, Long> = -1L to -1L
        val sentByNat = mutableSetOf<Pair<Long, Long>>()

        runNetwork(input,
                sendToNat = { x, y -> lastPacket = x to y },
                activateNat = { if (sentByNat.add(lastPacket)) lastPacket else return@solvePartTwo lastPacket.second })
        return "Failed"
    }

    private inline fun runNetwork(input: String, sendToNat: (Long, Long) -> Unit, activateNat: () -> Pair<Long, Long>) {
        val network = (0..49).map { IntCodeProgram.fromData(input).apply { input(it.toLong()) } }

        val idle = IntArray(50)

        while (true) {
            if (idle.all { it > 1 }) {
                val (x, y) = activateNat()
                idle[0] = 0
                network[0].input(x)
                network[0].input(y)
            }
            network.asSequence()
                    .withIndex()
                    .filter { idle[it.index] <= 1 }
                    .forEach { (address, p) ->

                        //RR scheduler
                        repeat(200) {
                            p.executeStep()
                            if (p.state == IntCodeProgram.State.WaitingForInput) {
                                idle[address]++
                                p.input(-1)
                                p.input(-1)
                                p.executeStep()
                            }
                        }

                        while (p.outputSize() >= 3) {
                            idle[address] = 0
                            val destination = p.output()!!.toInt()
                            if (destination == 255) {
                                sendToNat(p.output()!!, p.output()!!)
                            } else {
                                idle[destination] = 0
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
