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
                    next(Vec2(x, y), l)
                }
            }
        }
                .let { val e = emptyLevel(); if (it[0] == e) it else listOf(e) + it }
                .let { val e = emptyLevel(); if (it.last() == e) it else it + listOf(e) }
                .let { ErisianGrid(it) }

        private fun next(sq: Vec2, level: Int): Boolean = neighbors(sq, level) in 1..(2 - countBugs(sq, level))

        private fun neighbors(sq: Vec2, level: Int) = Direction.values().sumBy { neighborsInDirection(sq, level, it) }

        private fun neighborsInDirection(sq: Vec2, level: Int, direction: Direction): Int {
            val neighbor = sq + direction.vector
            return when {
                sq == center                               -> 0
                neighbor == center                         -> direction.edges.sumBy { countBugs(it, level + 1) }
                neighbor.x !in 0..4 || neighbor.y !in 0..4 -> countBugs(center + direction.vector, level - 1)
                else                                       -> countBugs(neighbor, level)
            }
        }

        private val Direction.edges: List<Vec2>
            get() = (0..4).map {
                when (this) {
                    Direction.LEFT  -> Vec2(4, it)
                    Direction.RIGHT -> Vec2(0, it)
                    Direction.UP    -> Vec2(it, 4)
                    Direction.DOWN  -> Vec2(it, 0)
                }
            }

        private fun countBugs(sq: Vec2, l: Int) = grid.getOrNull(l)
                ?.getOrNull(sq.y)
                ?.getOrNull(sq.x)
                .let { if (it == true) 1 else 0 }

        private val center = Vec2(2, 2)
    }
}
