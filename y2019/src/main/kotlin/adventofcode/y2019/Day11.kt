package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.language.intcode.IntCodeProgram
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.toGrid

fun main() = Day11.solve()

object Day11 : AdventSolution(2019, 11, "Space Police") {

    override fun solvePartOne(input: String) = mutableMapOf<Vec2, Long>()
            .apply { paint(input, this) }
            .size

    override fun solvePartTwo(input: String) = mutableMapOf(Vec2.origin to 1L)
            .apply { paint(input, this) }
            .mapValues { if (it.value == 1L) "██" else "  " }
            .toGrid("  ")
            .joinToString("\n", "\n") { it.joinToString("") }

    private fun paint(input: String, hull: MutableMap<Vec2, Long>) {
        var direction = Direction.UP
        var position = Vec2.origin
        val ant = IntCodeProgram.fromData(input)

        while (ant.state != IntCodeProgram.State.Halted) {
            ant.input(hull[position] ?: 0)
            ant.execute()
            ant.output()?.let { hull[position] = it }
            ant.output()?.let { direction = if (it == 0L) direction.turnLeft else direction.turnRight }
            position += direction.vector
        }
    }
}
