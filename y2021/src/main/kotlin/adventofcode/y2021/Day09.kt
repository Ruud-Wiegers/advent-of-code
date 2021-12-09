package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors

fun main() {
    Day09.solve()
}

object Day09 : AdventSolution(2021, 9, "Smoke Basin") {
    override fun solvePartOne(input: String): Int {
        val grid = Grid(parseInput(input))

        return grid.grid.indices.flatMap { y ->
            grid.grid[0].indices.map { x ->
                Vec2(x, y)
            }
        }.filter { it.neighbors().all { n -> (grid.get(n) ?: 10) > grid.get(it)!! } }
            .sumOf { grid.get(it)!! + 1 }


    }

    override fun solvePartTwo(input: String): Int {
        val grid = Grid(parseInput(input))

        val starts = grid.grid.indices.flatMap { y ->
            grid.grid[0].indices.map { x ->
                Vec2(x, y)
            }
        }.filter { it.neighbors().all { n -> (grid.get(n) ?: 10) > grid.get(it)!! } }

        val map = starts.map { bfs(grid, it) }
        return map.sorted().takeLast(3).reduce(Int::times)
    }


    private data class Grid(val grid: List<List<Int>>) {
        fun get(v: Vec2) = grid.getOrNull(v.y)?.getOrNull(v.x)
        fun neighbors(v: Vec2) = v.neighbors().mapNotNull { get(it) }

    }

    private fun parseInput(input: String) = input.lines().map { it.toList().map { it - '0' } }


    private fun bfs(grid: Grid, start: Vec2): Int {

        val open = mutableListOf(start)
        val closed = mutableSetOf<Vec2>()
        while (open.isNotEmpty()) {

            val candidate = open.removeLast()
            closed += candidate
            open += candidate.neighbors().filter { it !in closed }
                .filter { grid.get(it) in grid.get(candidate)!! until 9 }

        }
        return closed.size
    }

}