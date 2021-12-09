package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors

fun main()
{
    Day09.solve()
}

object Day09 : AdventSolution(2021, 9, "Smoke Basin")
{
    override fun solvePartOne(input: String): Int
    {
        val grid = Grid(parseInput(input))
        return grid.covering()
            .filter { it.neighbors().all { n -> (grid[n]) > grid[it] } }
            .sumOf { grid[it] + 1 }
    }

    override fun solvePartTwo(input: String): Int
    {
        val grid = Grid(parseInput(input))

        val lowPoints = grid.covering().filter { it.neighbors().all { n -> (grid[n]) > grid[it] } }

        return lowPoints.map { search(grid, it) }.sorted().takeLast(3).reduce(Int::times)
    }

    private fun parseInput(input: String) = input.lines().map { it.toList().map { it - '0' } }

    private data class Grid(val grid: List<List<Int>>)
    {
        operator fun get(v: Vec2) = grid.getOrNull(v.y)?.getOrNull(v.x) ?: 10

        fun covering() = grid.indices.flatMap { y ->
            grid[0].indices.map { x -> Vec2(x, y) }
        }
    }

    private fun search(grid: Grid, start: Vec2): Int
    {
        val open = mutableListOf(start)
        val closed = mutableSetOf<Vec2>()
        while (open.isNotEmpty())
        {
            val candidate = open.removeLast()
            closed += candidate
            open += candidate.neighbors().filter { it !in closed }.filter { grid[it] in grid[candidate] until 9 }
        }
        return closed.size
    }
}