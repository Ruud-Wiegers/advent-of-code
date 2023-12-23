package adventofcode.y2020

import adventofcode.io.AdventSolution

fun main() = Day17.solve()

object Day17 : AdventSolution(2020, 17, "Conway Cubes")
{
    override fun solvePartOne(input: String): Int
    {
        val active = parse(input).map { it + 0 }.toSet()

        return generateSequence(ConwayCube(active), ConwayCube::next).take(7).last().activeCells.size
    }

    override fun solvePartTwo(input: String): Int
    {
        val active = parse(input).map { it + 0 + 0 }.toSet()

        return generateSequence(ConwayCube(active), ConwayCube::next).take(7).last().activeCells.size
    }

    private fun parse(input:String): List<Coordinate> = buildList {
        input.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                if (ch == '#')
                    add(listOf(x, y))
            }
        }
    }

    private data class ConwayCube(val activeCells: Set<Coordinate>)
    {
        fun next(): ConwayCube = interior().filter(::aliveInNext).toSet().let(::ConwayCube)

        private fun aliveInNext(c: Coordinate): Boolean = countActiveNeighbors(c) in if (c in activeCells) 3..4 else 3..3

        private fun countActiveNeighbors(c: Coordinate): Int = neighbors
            .map { c.zip(it, Int::plus) }
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

private fun expand(ranges: List<IntRange>): List<Coordinate> =
    ranges.fold(listOf(emptyList())) { acc, r ->
        acc.flatMap { l -> r.map(l::plus) }
    }

private typealias Coordinate = List<Int>