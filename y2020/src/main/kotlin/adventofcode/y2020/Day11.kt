package adventofcode.y2020

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.mooreDelta
import adventofcode.util.vector.mooreNeighbors

fun main() = Day11.solve()

object Day11 : AdventSolution(2020, 11, "Seating System") {

    override fun solvePartOne(input: String) =
        generateSequence(input.let(::ConwayGrid), ConwayGrid::next)
            .zipWithNext()
            .first { (old, new) -> old == new }
            .first
            .count()

    override fun solvePartTwo(input: String): Any {

        return generateSequence(input.let(::SparseGrid), SparseGrid::next)
            .zipWithNext()
            .first { (old, new) -> old == new }
            .first
            .count()
    }


    private data class ConwayGrid(private val grid: List<List<Char>>) {

        constructor(input: String) : this(input.lines().map(String::toList))

        fun count() = grid.sumOf { it.count('#'::equals) }

        fun next() = grid.indices.map { y ->
            grid[0].indices.map { x ->
                typeInNextGeneration(Vec2(x, y), grid[y][x])
            }
        }.let(::ConwayGrid)

        private fun typeInNextGeneration(v: Vec2, ch: Char): Char =
            when (ch) {
                'L' -> if (countNear(v) > 0) 'L' else '#'
                '#' -> if (countNear(v) <= 4) '#' else 'L'
                else -> '.'
            }

        private fun countNear(v: Vec2) =
            v.mooreNeighbors()
                .map { grid.getOrNull(it.y)?.getOrNull(it.x) }
                .count { it == '#' }
    }


    private data class SparseGrid(private val grid: List<List<Char>>) {

        constructor(input: String) : this(input.lines().map(String::toList))

        fun count() = grid.sumOf { it.count('#'::equals) }

        fun next() = grid.indices.map { y ->
            grid[0].indices.map { x ->
                typeInNextGeneration(Vec2(x, y), grid[y][x])
            }
        }.let(::SparseGrid)

        private fun typeInNextGeneration(v: Vec2, ch: Char): Char =
            when (ch) {
                'L' -> if (countNear(v) > 0) 'L' else '#'
                '#' -> if (countNear(v) <= 5) '#' else 'L'
                else -> '.'
            }

        private fun countNear(v: Vec2): Int =
            mooreDelta.map { delta ->
                generateSequence(v, delta::plus)
                    .map { get(it) }
                    .drop(1)
                    .takeWhile { it != null }
                    .find { it != '.' }
            }
                .count { it == '#' }


        private fun get(v: Vec2) = grid.getOrNull(v.y)?.getOrNull(v.x)


    }
}
