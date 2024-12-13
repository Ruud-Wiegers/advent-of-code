package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.SparseGrid
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors

fun main() = Day12.solve()

object Day12 : AdventSolution(2024, 12, "Garden Groups") {

    override fun solvePartOne(input: String): Int {
        val garden = parseInput(input).toMutableMap()
        return findPlots(garden).sumOf { g -> g.size * siding(g) }
    }

    override fun solvePartTwo(input: String): Int {
        val garden = parseInput(input).toMutableMap()
        return findPlots(garden).sumOf { g -> g.size * bulkSiding(g) }
    }

    private fun parseInput(input: String): SparseGrid<Char> = input.lines()
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Vec2(x, y) to c }
        }
        .associate { it }

    private fun findPlots(grid: MutableMap<Vec2, Char>): List<MutableSet<Vec2>> = buildList {
        while (grid.isNotEmpty()) {
            val (pos, type) = grid.entries.first()
            val garden = mutableSetOf(pos)
            var open = mutableSetOf(pos)

            while (open.isNotEmpty()) {
                val c = open.take(1).single()
                open -= c
                val new = c.neighbors().filter { grid[it] == type }
                open += new.filter { it !in garden }
                garden += c
            }
            grid -= garden
            add(garden)
        }
    }

    private fun siding(plot: Set<Vec2>): Int = plot.sumOf { tile ->
        Direction.entries.count { direction -> plot.hasFence(tile, direction) }
    }

    private fun bulkSiding(plot: Set<Vec2>): Int = plot.sumOf { tile ->
        Direction.entries.count { direction ->
            val neighbor = tile + direction.turnLeft.vector
            plot.hasFence(tile, direction) && !plot.hasFence(neighbor, direction)
        }
    }

    private fun Set<Vec2>.hasFence(v: Vec2, direction: Direction): Boolean = v in this && v + direction.vector !in this
}