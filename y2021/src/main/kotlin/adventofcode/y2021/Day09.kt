package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors

object Day09 : AdventSolution(2021, 9, "Smoke Basin")
{
    override fun solvePartOne(input: String): Int
    {
        val grid = parseInput(input)
        val lowPoints = grid.covering().filter { it.neighbors().all { n -> grid[n] > grid[it] } }
        return lowPoints.sumOf { grid[it] + 1 }
    }

    override fun solvePartTwo(input: String): Int
    {
        val grid = parseInput(input)
        val lowPoints = grid.covering().filter { it.neighbors().all { n -> grid[n] > grid[it] } }
        fun next(source: Vec2) = source.neighbors().filter { n -> grid[n] in grid[source] until grid.maxHeight }
        return lowPoints.map { start -> findReachable(start, ::next).size }.sorted().takeLast(3).reduce(Int::times)
    }

    private fun parseInput(input: String) = input.lines().map { it.toList().map { it - '0' } }.let(::Grid)

    private data class Grid(private val grid: List<List<Int>>)
    {
        val maxHeight = 9
        operator fun get(v: Vec2) = grid.getOrNull(v.y)?.getOrNull(v.x) ?: maxHeight
        fun covering() = grid.indices.flatMap { y -> grid[0].indices.map { x -> Vec2(x, y) } }
    }
}

private fun <T> findReachable(start: T, next: (source: T) -> Iterable<T>): Set<T>
{
    val open = mutableListOf(start)
    val closed = mutableSetOf<T>()
    while (open.isNotEmpty())
    {
        val candidate = open.removeLast()
        closed += candidate
        open += next(candidate).filter { it !in closed }
    }
    return closed
}