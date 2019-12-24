package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import adventofcode.y2017.takeWhileDistinct

fun main() = Day24.solve()

object Day24 : AdventSolution(2019, 24, "Planet of Discord") {

    override fun solvePartOne(input: String) =
            generateSequence(ConwayGrid(input), ConwayGrid::next)
                    .takeWhileDistinct()
                    .last()
                    .next()
                    .resourceValue()

    private data class ConwayGrid(private val grid: List<List<Boolean>>) {
        constructor(input: String) : this(input.lines().map { it.map { it == '#' } })

        fun resourceValue() = grid.flatten().map { if (it) 1L else 0L }.reversed().fold(0L) { acc, it -> acc * 2 + it }

        fun next() = grid.indices.map { y ->
            grid[0].indices.map { x ->
                next(x, y)
            }
        }.let(::ConwayGrid)

        private fun next(x: Int, y: Int): Boolean =
                if (grid[y][x])
                    adjacentBugs(x, y) == 1
                else
                    adjacentBugs(x, y) in 1..2

        private fun adjacentBugs(x: Int, y: Int) = Direction.values().map { Vec2(x, y) + it.vector }.count { (a, b) -> isBug(a, b) }

        private fun isBug(x: Int, y: Int) = grid.getOrNull(y)?.getOrNull(x) == true
    }


    override fun solvePartTwo(input: String) = generateSequence(ErisianGrid(input), ErisianGrid::next)
            .drop(200)
            .first()
            .grid
            .flatten()
            .flatten()
            .count { it }

    private fun emptyLevel() = List(5) { List(5) { false } }

    private data class ErisianGrid(val grid: List<List<List<Boolean>>>) {
        constructor(input: String) : this(listOf(emptyLevel()) +
                listOf(input.lines().map { it.map { it == '#' } }) +
                listOf(emptyLevel()))

        fun next() = grid.indices.map { l ->
            grid[0].indices.map { y ->
                grid[0][0].indices.map { x ->
                    next(l, y, x)
                }
            }
        }
                .let { val e = emptyLevel(); if (it[0] == e) it else listOf(e) + it }
                .let { val e = emptyLevel(); if (it.last() == e) it else it + listOf(e) }
                .let { ErisianGrid(it) }

        private fun next(l: Int, y: Int, x: Int): Boolean = if (grid[l][y][x])
            neighbors(l, y, x).count { it } == 1
        else
            neighbors(l, y, x).count { it } in 1..2


        private fun neighbors(l: Int, y: Int, x: Int): List<Boolean> = neighborsL(l, y, x) + neighborsR(l, y, x) + neighborsU(l, y, x) + neighborsD(l, y, x)

        private fun neighborsL(l: Int, y: Int, x: Int): List<Boolean> = when {
            x == 0           -> grid.getOrNull(l - 1)?.let { listOf(it[2][1]) }.orEmpty()
            x == 2 && y == 2 -> emptyList()
            x == 3 && y == 2 -> grid.getOrNull(l + 1).orEmpty().map { it.last() }
            else             -> listOf(grid[l][y][x - 1])
        }

        private fun neighborsR(l: Int, y: Int, x: Int): List<Boolean> = when {
            x == 4           -> grid.getOrNull(l - 1)?.let { listOf(it[2][3]) }.orEmpty()
            x == 2 && y == 2 -> emptyList()
            x == 1 && y == 2 -> grid.getOrNull(l + 1).orEmpty().map { it.first() }
            else             -> listOf(grid[l][y][x + 1])
        }

        private fun neighborsU(l: Int, y: Int, x: Int): List<Boolean> = when {
            y == 0           -> grid.getOrNull(l - 1)?.let { listOf(it[1][2]) }.orEmpty()
            x == 2 && y == 2 -> emptyList()
            x == 2 && y == 3 -> grid.getOrNull(l + 1)?.last().orEmpty()
            else             -> listOf(grid[l][y - 1][x])
        }

        private fun neighborsD(l: Int, y: Int, x: Int): List<Boolean> = when {
            y == 4           -> grid.getOrNull(l - 1)?.let { listOf(it[3][2]) }.orEmpty()
            x == 2 && y == 2 -> emptyList()
            x == 2 && y == 1 -> grid.getOrNull(l + 1)?.first().orEmpty()
            else             -> listOf(grid[l][y + 1][x])
        }
    }
}
