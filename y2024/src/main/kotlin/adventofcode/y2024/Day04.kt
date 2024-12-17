package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.SparseGrid
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.mooreDelta

fun main() {
    Day04.solve()
}

object Day04 : AdventSolution(2024, 4, "Ceres Search") {

    override fun solvePartOne(input: String): Int {
        val grid = parseInput(input)

        return grid.keys.sumOf { start ->
            mooreDelta.count { dir ->
                generateSequence(start, dir::plus).take(4)
                    .mapNotNull(grid::get)
                    .joinToString("") == "XMAS"
            }
        }
    }

    override fun solvePartTwo(input: String): Int {
        val grid: SparseGrid<Char> = parseInput(input)

        val corners = listOf(Vec2(-1, -1), Vec2(-1, 1), Vec2(1, -1), Vec2(1, 1))

        return grid.keys.count { center ->
            corners.count { corner ->
                grid[center - corner] == 'M'
                        && grid[center] == 'A'
                        && grid[center + corner] == 'S'
            } == 2
        }
    }

    private fun parseInput(input: String): Map<Vec2, Char> = input.lines()
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Vec2(x, y) to c }
        }
        .toMap()
}