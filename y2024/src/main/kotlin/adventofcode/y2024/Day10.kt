package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.SparseGrid
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors

fun main() {
    Day10.solve()
}

object Day10 : AdventSolution(2024, 10, "Hoof It") {

    override fun solvePartOne(input: String): Int {
        val grid: SparseGrid<Int> = parseInput(input)

        return grid.filter { it.value == 0 }.keys.sumOf { trailhead ->
            findAllSummits(grid, trailhead).distinct().size
        }
    }

    override fun solvePartTwo(input: String): Int {
        val grid: SparseGrid<Int> = parseInput(input)

        return grid.filter { it.value == 0 }.keys.sumOf { trailhead ->
            findAllSummits(grid, trailhead).size
        }
    }
}

private fun parseInput(input: String): SparseGrid<Int> = input.lines()
    .flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> Vec2(x, y) to c.digitToInt() }
    }
    .associate { it }


private fun findAllSummits(grid: SparseGrid<Int>, trailhead: Vec2): List<Vec2> = generateSequence(listOf(trailhead)) {
    it.flatMap { prev ->
        val expectedHeight = grid.getValue(prev) + 1
        prev.neighbors().filter { new -> grid[new] == expectedHeight }
    }
}
    .elementAt(9)
