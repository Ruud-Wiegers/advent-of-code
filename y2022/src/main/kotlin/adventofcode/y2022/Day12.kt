package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors
import java.lang.IllegalStateException

object Day12 : AdventSolution(2022, 12, "Hill Climbing Algorithm") {

    override fun solvePartOne(input: String): Int {
        val landscape = parse(input)

        var open = listOf(landscape.start)
        val closed = mutableSetOf(landscape.start)
        var count = 0
        do {
            count++
            val new = open.flatMap { landscape.neighbors(it) }
                .distinct()
                .filter { it !in closed }
            closed += new
            open = new
        } while (landscape.end !in new)
        return count
    }

    override fun solvePartTwo(input: String): Int {
        val landscape = parse(input)

        var open = listOf(landscape.end)
        val closed = mutableSetOf(landscape.end)
        var count = 0
        do {
            count++
            val new = open.flatMap { landscape.inverseNeighbors(it) }
                .distinct()
                .filter { it !in closed }
            closed += new
            open = new
        } while (new.none { landscape.landscape[it.y][it.x] == 0 })
        return count
    }
}


private fun parse(input: String): Landscape {
    var start: Vec2? = null
    var end: Vec2? = null
    val terrain = input.lines().mapIndexed { row, line ->
        line.mapIndexed { col, ch ->
            when (ch) {
                'S' -> 0.also { start = Vec2(col, row) }
                'E' -> 25.also { end = Vec2(col, row) }
                else -> ch - 'a'
            }
        }
    }

    return Landscape(start!!, end!!, terrain)
}


private data class Landscape(val start: Vec2, val end: Vec2, val landscape: List<List<Int>>) {

    fun neighbors(pos: Vec2) = pos.neighbors()
        .filter { (x, y) -> y in landscape.indices && x in landscape[y].indices }
        .filter { (x, y) -> landscape[pos.y][pos.x] + 1 >= landscape[y][x] }

    fun inverseNeighbors(pos: Vec2) = pos.neighbors()
        .filter { (x, y) -> y in landscape.indices && x in landscape[y].indices }
        .filter { (x, y) -> landscape[y][x] + 1 >= landscape[pos.y][pos.x] }
}
