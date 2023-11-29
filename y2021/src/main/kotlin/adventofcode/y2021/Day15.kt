package adventofcode.y2021

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors
import java.util.*

object Day15 : AdventSolution(2021, 15, "Chitons") {
    override fun solvePartOne(input: String): Int {
        val grid = parse(input)
        return findAllPaths(grid, Vec2.origin)[Vec2(grid[0].lastIndex, grid.lastIndex)]
    }

    override fun solvePartTwo(input: String): Int {
        val grid = parse(input)

        val size = grid.size
        val bigGrid = List(size * 5) { y ->
            IntArray(grid[0].size * 5) { x ->
                (grid[y % size][x % size] + y / size + x / size - 1) % 9 + 1
            }
        }

        return findAllPaths(bigGrid, Vec2.origin)[Vec2(bigGrid[0].lastIndex, bigGrid.lastIndex)]
    }

    private fun parse(input: String) = input.lines().map { it.map { it - '0' }.toIntArray() }

    private fun findAllPaths(costs: List<IntArray>, start: Vec2): List<IntArray> {
        val distances = costs.map { IntArray(it.size) { Int.MAX_VALUE } }
        distances[start] = 0

        val open = PriorityQueue(compareBy<Vec2> { distances[it] })
        open += start

        while (open.isNotEmpty()) {
            val edge = open.poll()

            for (neighbor in edge.neighbors()) {
                if (neighbor !in costs) continue

                val newDistance = distances[edge] + costs[neighbor]
                if (distances[neighbor] > newDistance) {
                    distances[neighbor] = newDistance
                    open += neighbor
                }
            }
        }

        return distances
    }

    private operator fun List<IntArray>.contains(v: Vec2) = v.y in indices && v.x in get(0).indices
    private operator fun List<IntArray>.get(v: Vec2) = this[v.y][v.x]
    private operator fun List<IntArray>.set(v: Vec2, value: Int) {
        this[v.y][v.x] = value
    }
}