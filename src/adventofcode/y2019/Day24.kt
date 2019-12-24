package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
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

        private fun adjacentBugs(x: Int, y: Int) = bugCount(x + 1, y) + bugCount(x - 1, y) + bugCount(x, y + 1) + bugCount(x, y - 1)

        private fun bugCount(x: Int, y: Int) = if (grid.getOrNull(y)?.getOrNull(x) == true) 1 else 0
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

        private fun next(l: Int, y: Int, x: Int): Boolean =
                neighbors(l, y, x) in 1..(2 - countBugs(l, y, x))

        private fun neighbors(l: Int, y: Int, x: Int) =
                left(l, y, x) + right(l, y, x) + up(l, y, x) + down(l, y, x)


        private fun left(l: Int, y: Int, x: Int) = when {
            x == 2 && y == 2 -> 0
            x == 3 && y == 2 -> (0..4).sumBy { countBugs(l + 1, it, 4) }
            x == 0           -> countBugs(l - 1, 2, 1)
            else             -> countBugs(l, y, x - 1)
        }

        private fun right(l: Int, y: Int, x: Int) = when {
            x == 2 && y == 2 -> 0
            x == 1 && y == 2 -> (0..4).sumBy { countBugs(l + 1, it, 0) }
            x == 4           -> countBugs(l - 1, 2, 3)
            else             -> countBugs(l, y, x + 1)
        }

        private fun up(l: Int, y: Int, x: Int) = when {
            y == 2 && x == 2 -> 0
            y == 3 && x == 2 -> (0..4).sumBy { countBugs(l + 1, 4, it) }
            y == 0           -> countBugs(l - 1, 1, 2)
            else             -> countBugs(l, y - 1, x)
        }

        private fun down(l: Int, y: Int, x: Int) = when {
            y == 2 && x == 2 -> 0
            y == 1 && x == 2 -> (0..4).sumBy { countBugs(l + 1, 0, it) }
            y == 4           -> countBugs(l - 1, 3, 2)
            else             -> countBugs(l, y + 1, x)
        }

        private fun countBugs(l: Int, y: Int, x: Int) =
                grid.getOrNull(l)?.getOrNull(y)?.getOrNull(x).let { if (it == true) 1 else 0 }

    }
}
