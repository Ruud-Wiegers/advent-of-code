package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day17.solve()

object Day17 : AdventSolution(2020, 17, "Conway Cubes")
{
    override fun solvePartOne(input: String): Int
    {
        val active = buildSet {
            input.lines().forEachIndexed { y, line ->
                line.forEachIndexed { x, ch ->
                    if (ch == '#')
                        add(listOf(x, y, 0))
                }
            }
        }

        return generateSequence(ConwayCube(active), ConwayCube::next)
            .take(7).last()
            .activeCells.size
    }

    override fun solvePartTwo(input: String): Any
    {
        val active = buildSet {
            input.lines().forEachIndexed { y, line ->
                line.forEachIndexed { x, ch ->
                    if (ch == '#')
                        add(listOf(x, y, 0, 0))
                }
            }
        }

        return generateSequence(ConwayCube(active), ConwayCube::next)
            .take(7).last()
            .activeCells.size
    }

    private data class ConwayCube(val activeCells: Set<Coordinate>)
    {
        fun next() = interior().filter(::aliveInNext).toSet().let(::ConwayCube)

        private fun aliveInNext(c: Coordinate): Boolean = c.countActiveNeighbors() in if (c in activeCells) 3..4 else 3..3

        private fun Coordinate.countActiveNeighbors() = neighbors
            .map { this.zip(it, Int::plus) }
            .count { it in activeCells }

        private val neighbors: List<Coordinate> by lazy { expand(activeCells.first().map { -1..1 }) }

        private fun interior(): List<Coordinate>
        {
            val min = activeCells.reduce { a, b -> a.zip(b, ::minOf) }.map { it - 1 }
            val max = activeCells.reduce { a, b -> a.zip(b, ::maxOf) }.map { it + 1 }

            return expand(min.zip(max, Int::rangeTo))
        }
    }
}

private fun expand(r: List<IntRange>): List<Coordinate> =
    if (r.size == 1)
        r.single().map(::listOf)
    else
        r.last().flatMap { v -> expand(r.dropLast(1)).map { it + v } }


private typealias Coordinate = List<Int>
