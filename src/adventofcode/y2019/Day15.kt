package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.toGrid

fun main() = Day15.solve()

object Day15 : AdventSolution(2019, 15, "Sunday") {

    override fun solvePartOne(input: String) = IntCodeProgram.fromData(input).let(::exploreMaze).first

    override fun solvePartTwo(input: String): Any? {
        val map = IntCodeProgram.fromData(input).let(::exploreMaze).second

        val oxygen = map.asSequence().first { it.value == 2 }.key

        val open = map.filterValues { it != 0 }.keys.toMutableSet()

        return generateSequence(listOf(oxygen)) { candidates ->
            open -= candidates
            candidates.flatMap { p -> Direction.values().map { p + it.vector } }
                    .filter { it in open }
        }
                .takeWhile { it.isNotEmpty() }
                .count() - 1
    }

    private fun exploreMaze(droidcontrol: IntCodeProgram): Pair<Int, MutableMap<Vec2, Int>> {
        val map = mutableMapOf(Vec2.origin to 1)
        val path = mutableListOf(Vec2.origin)
        val steps = mutableListOf<Direction>()
        var score = 0

        while (path.isNotEmpty()) {
            val dir = Direction.values().find { path.last() + it.vector !in map }

            dir?.let {
                val n = path.last() + it.vector
                path += n
                steps += it
                droidcontrol.input(it.toInput())
            }

            if (dir == null) {
                path.removeAt(path.lastIndex)
                if (path.isEmpty()) break

                val reverse = steps.removeAt(steps.lastIndex).reverse

                droidcontrol.input(reverse.toInput())
            }
            droidcontrol.execute()
            val out = droidcontrol.output()!!.toInt()

            map[path.last()] = out
            when (out) {
                0 -> {
                    path.removeAt(path.lastIndex)
                    steps.removeAt(steps.lastIndex)
                }
                2 -> score = path.size - 1
            }
        }
        return Pair(score, map)
    }

    private fun Direction.toInput() = when (this) {
        Direction.UP -> 1L
        Direction.RIGHT -> 4L
        Direction.DOWN -> 2L
        Direction.LEFT -> 3L
    }

    private fun printMap(map: MutableMap<Vec2, Int>) {
        map[Vec2.origin] = 3

        map.mapValues { (_, v) ->
            when (v) {
                1 -> "  "
                2 -> "OX"
                3 -> "ST"
                else -> "██"
            }
        }
                .toGrid("??")
                .joinToString("\n") { it.joinToString("") }
                .let(::println)

    }
}
