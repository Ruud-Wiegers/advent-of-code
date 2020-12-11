package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.collections.takeWhileDistinct

fun main() = Day11.solve()

object Day11 : AdventSolution(2020, 11, "Seating System")
{
    override fun solvePartOne(input: String): Any?
    {
        val grid = input.let(::ConwayGrid)

        return generateSequence(grid, ConwayGrid::next)
            .takeWhileDistinct()
            .last()
            .resourceValue()
    }

    private data class ConwayGrid(private val grid: List<List<Char>>)
    {

        constructor(input: String) : this(input.lines().map(String::toList))

        fun resourceValue() = count('#')

        private fun count(ch: Char) = grid.flatten().count { it == ch }

        fun next() = grid.indices.map { y ->
            grid[0].indices.map { x ->
                typeInNextGeneration(x, y)
            }
        }.let(::ConwayGrid)

        private fun typeInNextGeneration(x: Int, y: Int): Char =
            when (grid[y][x])
            {
                '.'  -> '.'
                'L'  -> if (countNear(x, y, '#') == 0) '#' else 'L'
                '#'  -> if (countNear(x, y, '#') > 4) 'L' else '#'
                else -> throw IllegalStateException()
            }

        private fun countNear(x: Int, y: Int, c: Char): Int
        {
            var count = 0
            for (j in y - 1..y + 1)
                for (i in x - 1..x + 1)
                    if (grid.getOrNull(j)?.getOrNull(i) == c)
                        count++
            return count
        }
    }

    override fun solvePartTwo(input: String): Any?
    {
        val grid = input.let(::ConwayGrid2)

        return generateSequence(grid, ConwayGrid2::next)
            .takeWhileDistinct()
            .last()
            .resourceValue()
    }

    private data class ConwayGrid2(private val grid: List<List<Char>>)
    {

        constructor(input: String) : this(input.lines().map(String::toList))

        fun resourceValue() = count('#')

        private fun count(ch: Char) = grid.flatten().count { it == ch }

        fun next() = grid.indices.map { y ->
            grid[0].indices.map { x ->
                typeInNextGeneration(x, y)
            }
        }.let(::ConwayGrid2)

        private fun typeInNextGeneration(x: Int, y: Int): Char =
            when (grid[y][x])
            {
                '.'  -> '.'
                'L'  -> if (countNear(x, y, '#') == 0) '#' else 'L'
                '#'  -> if (countNear(x, y, '#') > 5) 'L' else '#'
                else -> throw IllegalStateException()
            }

        private fun countNear(x: Int, y: Int, c: Char): Int
        {
            var count = 0
            for (dx in -1..1)
                for (dy in -1..1)
                    generateSequence(Pair(dx, dy)) { (i, j) -> Pair(i + dx, j + dy) }
                        .map { (i, j) -> grid.getOrNull(j + y)?.getOrNull(i + x) }
                        .takeWhile { it != null }
                        .firstOrNull { it != '.' }
                        .also { if (it == c) count++ }

            return count
        }
    }
}
