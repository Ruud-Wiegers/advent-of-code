package adventofcode.y2015

import adventofcode.AdventSolution
import adventofcode.y2015.Day06.Action.*

object Day06 : AdventSolution(2015, 6, "Probably a Fire Hazard") {

    override fun solvePartOne(input: String): Int =
            execute(parse(input)) { v, a ->
                when (a) {
                    ON -> 1
                    OFF -> 0
                    TOGGLE -> 1 - v
                }
            }

    override fun solvePartTwo(input: String): Int =
            execute(parse(input)) { v, a ->
                when (a) {
                    ON -> v + 1
                    OFF -> maxOf(v - 1, 0)
                    TOGGLE -> v + 2
                }
            }

    private inline fun execute(instructions: Sequence<Instruction>, execute: (v: Int, a: Action) -> Int): Int {
        val screen = Array(1000) { IntArray(1000) }

        for (instruction in instructions) {
            for (y in instruction.y1..instruction.y2) {
                val row = screen[y]
                for (x in instruction.x1..instruction.x2)
                    row[x] = execute(row[x], instruction.a)
            }
        }

        return screen.sumBy(IntArray::sum)
    }

    private fun parse(input: String): Sequence<Instruction> {
        val regex = "(.*) (\\d+),(\\d+) through (\\d+),(\\d+)".toRegex()
        return input.splitToSequence("\n")
                .map { regex.matchEntire(it)!!.destructured }
                .map { (a, x1, y1, x2, y2) ->
                    val act = when (a) {
                        "turn on" -> ON
                        "turn off" -> OFF
                        "toggle" -> TOGGLE
                        else -> throw IllegalArgumentException(a)
                    }
                    Instruction(act, x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
                }
    }

    enum class Action { ON, OFF, TOGGLE }
    data class Instruction(val a: Action, val x1: Int, val y1: Int, val x2: Int, val y2: Int)
}
