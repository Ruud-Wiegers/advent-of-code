package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.collections.cartesian
import adventofcode.util.collections.takeWhileDistinct
import adventofcode.util.vector.Vec2

fun main() = Day11.solve()

object Day11 : AdventSolution(2020, 11, "Seating System") {
    override fun solvePartOne(input: String): Any? {
        val grid = input.let(::ConwayGrid)

        return generateSequence(grid, ConwayGrid::next)
            .takeWhileDistinct()
            .last()
            .count('#')
    }

    private data class ConwayGrid(private val grid: List<List<Char>>) {

        constructor(input: String) : this(input.lines().map(String::toList))

        fun count(ch: Char) = grid.flatten().count { it == ch }

        fun next() = grid.indices.map { y ->
            grid[0].indices.map { x ->
                typeInNextGeneration(x, y)
            }
        }.let(::ConwayGrid)

        private fun typeInNextGeneration(x: Int, y: Int): Char =
            when (grid[y][x]) {
                '.'  -> '.'
                'L'  -> if (countNear(x, y, '#') == 0) '#' else 'L'
                '#'  -> if (countNear(x, y, '#') > 4) 'L' else '#'
                else -> throw IllegalStateException()
            }

        private fun countNear(x: Int, y: Int, c: Char): Int {
            var count = 0
            for (j in y - 1..y + 1)
                for (i in x - 1..x + 1)
                    if (grid.getOrNull(j)?.getOrNull(i) == c)
                        count++
            return count
        }
    }

    override fun solvePartTwo(input: String): Any? {
        val grid = fromInput(input)

        return generateSequence(grid, SparseGrid::next)
            .takeWhileDistinct()
            .last()
            .count()
    }

    private fun fromInput(input: String) = input.lines()
        .flatMapIndexed { y, l ->
            l.mapIndexed { x, ch -> Vec2(x, y) to ch }
        }
        .toMap()
        .let(::SparseGrid)

    private data class SparseGrid(private val grid: Map<Vec2, Char>) {

        fun count() = grid.count { it.value == '#' }

        fun next() = grid.mapValues { (k, v) -> typeInNextGeneration(k, v) }.let(::SparseGrid)

        private fun typeInNextGeneration(v: Vec2, ch: Char): Char =
            when (ch) {
                '.'  -> '.'
                'L'  -> if (countNear(v) > 0) 'L' else '#'
                else -> if (countNear(v) <= 5) '#' else 'L'
            }

        private fun countNear(v: Vec2) =
            (-1..1).cartesian()
                .map { Vec2(it.first, it.second) }
                .map { delta ->
                    generateSequence(v) { it + delta }
                        .map { grid[it] }
                        .takeWhile { it != null }
                        .find { it != '.' }
                }
                .count { it == '#' }

    }
}
