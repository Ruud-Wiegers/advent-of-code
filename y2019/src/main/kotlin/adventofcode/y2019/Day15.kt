package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors
import java.util.*

fun main() = Day15.solve()

object Day15 : AdventSolution(2019, 15, "Oxygen System") {

    override fun solvePartOne(input: String) =
        IntCodeProgram.fromData(input).let(::exploreMaze).first

    override fun solvePartTwo(input: String): Int {
        val map: Map<Vec2, Int> = IntCodeProgram.fromData(input).let(::exploreMaze).second

        val source: List<Vec2> = map.asSequence().first { it.value == 2 }.key.let { listOf(it) }
        val open: MutableSet<Vec2> = map.asSequence().filter { it.value == 1 }.map { it.key }.toMutableSet()

        return generateSequence(source) { candidates ->
            candidates.flatMap { p -> p.neighbors().filter { it in open } }
        }
            .onEach { open -= it.toSet() }
            .indexOfFirst { it.isEmpty() } - 1
    }

    private fun exploreMaze(droidcontrol: IntCodeProgram): Pair<Int, Map<Vec2, Int>> {
        val map = mutableMapOf(Vec2.origin to 1)
        var position = Vec2.origin
        val pathToPosition = Stack<Direction>()
        var score = 0

        while (true) {
            val forwardDirection = Direction.values().find { position + it.vector !in map }
            val backtrackingDirection = pathToPosition.lastOrNull()?.reverse
            val dir = forwardDirection ?: backtrackingDirection ?: return score to map

            droidcontrol.input(dir.toInput())
            droidcontrol.execute()
            val status = droidcontrol.output()!!.toInt()

            position += dir.vector

            map[position] = status

            when {
                status == 0              -> position -= dir.vector
                forwardDirection != null -> pathToPosition.push(dir)
                else                     -> pathToPosition.pop()
            }
            if (status == 2) score = pathToPosition.size
        }
    }

    private fun Direction.toInput() = when (this) {
        Direction.UP    -> 1L
        Direction.RIGHT -> 4L
        Direction.DOWN  -> 2L
        Direction.LEFT  -> 3L
    }
}
