package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors

fun main() {
    Day12.solve()
}

object Day12 : AdventSolution(2022, 12, "Hill Climbing Algorithm") {

    override fun solvePartOne(input: String): Int {
        val landscape = parse(input)
        return bfs(listOf(landscape.start), landscape.end, landscape::reachableNeighbors)
    }

    override fun solvePartTwo(input: String): Int {
        val landscape = parse(input)
        return bfs(landscape.getLowestPoints(), landscape.end, landscape::reachableNeighbors)
    }

    private fun bfs(start: Iterable<Vec2>, end: Vec2, reachableNeighbors: (Vec2) -> Iterable<Vec2>): Int =
        generateSequence(Pair(start.toSet(), start.toSet())) { (open, closed) ->
            val new = open.flatMap(reachableNeighbors).toSet() - closed
            Pair(new, closed + new)
        }
            .indexOfFirst { (open, _) -> end in open }
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


private data class Landscape(val start: Vec2, val end: Vec2, private val heights: List<List<Int>>) {

    private operator fun contains(pos: Vec2) = pos.y in heights.indices && pos.x in heights[pos.y].indices

    private fun height(pos: Vec2) = heights[pos.y][pos.x]

    fun reachableNeighbors(pos: Vec2) = pos.neighbors()
        .filter { it in this }
        .filter { height(pos) + 1 >= height(it) }

    fun getLowestPoints() =
        heights.indices.flatMap { y ->
            heights[0].indices.map { x ->
                Vec2(x, y)
            }
        }
            .filter { height(it) == 0 }

}
