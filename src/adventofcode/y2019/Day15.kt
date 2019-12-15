package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram

fun main() = Day15.solve()

object Day15 : AdventSolution(2019, 15, "Sunday") {

    override fun solvePartOne(input: String): Any? {
        val droidcontrol: IntCodeProgram = parseProgram(input)

        val (map, score) = exploreMaze(droidcontrol)
        printMap(map)
        return score
    }

    override fun solvePartTwo(input: String): Any? {
        val droidcontrol: IntCodeProgram = parseProgram(input)

        val map = exploreMaze(droidcontrol).first

        var candidates = listOf(map.asSequence().first { it.value == 2 }.key)
        val open = map.filterValues { it != 0 }.keys.toMutableSet()

        var count = 0

        while (open.isNotEmpty()) {
            val new = candidates
                    .flatMap { p -> Direction.values().map { p + it.vector } }
                    .filter { it in open }
            open -= new
            candidates = new
            count++
        }

        return count
    }

    private fun exploreMaze(droidcontrol: IntCodeProgram): Pair<MutableMap<Point, Int>, Int> {
        val map = mutableMapOf(Point(21, 21) to 1)
        val path = mutableListOf(Point(21, 21))
        val steps = mutableListOf<Direction>()
        var score = 0

        while (path.isNotEmpty()) {
            val dir = Direction.values().find { path.last() + it.vector !in map }

            dir?.let {
                val n = path.last() + it.vector
                path += n
                steps += it
                droidcontrol.input(when (it) {
                    Direction.UP -> 1
                    Direction.RIGHT -> 4
                    Direction.DOWN -> 2
                    Direction.LEFT -> 3
                })
            }

            if (dir == null) {
                path.removeAt(path.lastIndex)
                if (path.isEmpty()) break

                droidcontrol.input(when (steps.removeAt(steps.lastIndex)) {
                    Direction.UP -> 2
                    Direction.RIGHT -> 3
                    Direction.DOWN -> 1
                    Direction.LEFT -> 4
                })
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
        return Pair(map, score)
    }

    private fun printMap(map: MutableMap<Point, Int>) {
        map[Point(21, 21)] = 3
        for (y in 0..map.maxBy { it.key.y }!!.key.y) {
            for (x in 0..map.maxBy { it.key.x }!!.key.x) {
                print(when (map[Point(x, y)]) {
                    1 -> "  "
                    2 -> "()"
                    3 -> "<>"
                    else -> "██"
                })
            }
            println()

        }
    }

    private fun parseProgram(data: String) = data
            .split(',')
            .map(String::toLong)
            .let { IntCodeProgram(it) }

    private data class Point(val x: Int, val y: Int) {
        operator fun plus(o: Point) = Point(x + o.x, y + o.y)
        operator fun minus(o: Point) = Point(x - o.x, y - o.y)
    }

    private enum class Direction(val vector: Point) {
        UP(Point(0, -1)), RIGHT(Point(1, 0)), DOWN(Point(0, 1)), LEFT(Point(-1, 0));
    }
}
