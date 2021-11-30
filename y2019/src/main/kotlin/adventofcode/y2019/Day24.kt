package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.collections.takeWhileDistinct
import adventofcode.util.vector.Vec3

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


    override fun solvePartTwo(input: String) = generateSequence(ErisianGrid(toGrid(input)), ErisianGrid::next)
            .drop(200)
            .first()
            .grid
            .size

    private fun toGrid(input: String) = sequence {
        val g = input.lines()
        for (y in 0 until 5)
            for (x in 0 until 5)
                if (g[y][x] == '#') yield(Vec3(x, y, 0))
    }.toSet()

    private data class ErisianGrid(val grid: Set<Vec3>) {
        val zs: IntRange by lazy { grid.minOf(Vec3::z) - 1..grid.maxOf(Vec3::z) + 1 }

        fun next() = sequence {
            for (z in zs)
                for (y in 0..4)
                    for (x in 0..4)
                        yield(Vec3(x, y, z))
        }
                .filter { next(it) }
                .toSet()
                .let { ErisianGrid(it) }

        private fun next(sq: Vec3): Boolean = sq.neighbors() in if (sq in grid) 1..1 else 1..2


        private fun Vec3.neighbors() = left() + right() + up() + down()

        private fun Vec3.left() = when {
            x == 2 && y == 2 -> 0
            x == 3 && y == 2 -> (0..4).sumOf { countBugs(Vec3(4, it, z + 1)) }
            x == 0           -> countBugs(Vec3(1, 2, z - 1))
            else             -> countBugs(copy(x = x - 1))
        }

        private fun Vec3.right() = when {
            x == 2 && y == 2 -> 0
            x == 1 && y == 2 -> (0..4).sumOf { countBugs(Vec3(0, it, z + 1)) }
            x == 4           -> countBugs(Vec3(3, 2, z - 1))
            else             -> countBugs(copy(x = x + 1))
        }

        private fun Vec3.up() = when {
            y == 2 && x == 2 -> 0
            y == 3 && x == 2 -> (0..4).sumOf { countBugs(Vec3(it, 4, z + 1)) }
            y == 0           -> countBugs(Vec3(2, 1, z - 1))
            else             -> countBugs(copy(y = y - 1))
        }

        private fun Vec3.down() = when {
            y == 2 && x == 2 -> 0
            y == 1 && x == 2 -> (0..4).sumOf { countBugs(Vec3(it, 0, z + 1)) }
            y == 4           -> countBugs(Vec3(2, 3, z - 1))
            else             -> countBugs(copy(y = y + 1))
        }

        private fun countBugs(sq: Vec3) = if (sq in grid) 1 else 0
    }
}
