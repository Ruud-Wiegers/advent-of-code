package adventofcode.y2025

import adventofcode.io.AdventSolution
import adventofcode.util.algorithm.UnionFind
import adventofcode.util.vector.Vec3

fun main() {
    Day08.solve()
}

object Day08 : AdventSolution(2025, 8, "Playground") {

    override fun solvePartOne(input: String): Long = solvePartOne(input, 1000)

    fun solvePartOne(input: String, connectionCount: Int): Long {
        val junctions = parse(input)

        val distances = distances(junctions).take(connectionCount)

        val unionFind = UnionFind(junctions.size)
        distances.forEach { unionFind.union(it.fromIndex, it.toIndex) }

        val results = unionFind.toSets()
        val largestGroupSizes = results.map { it.size }.sortedDescending().take(3)
        return largestGroupSizes.map(Int::toLong).reduce(Long::times)
    }

    override fun solvePartTwo(input: String): Long {
        val junctions = parse(input)

        val distances = distances(junctions)
        val sets = UnionFind(junctions.size)

        distances.forEach { (fromId, toId) ->
            sets.union(fromId, toId)
            if (sets.setCount == 1) {
                return junctions[fromId].x.toLong() * junctions[toId].x.toLong()
            }
        }

        error("no solution")
    }
}

private fun parse(input: String): List<Vec3> = input
    .lines()
    .map { it.split(',').map(String::toInt) }
    .map { (x, y, z) -> Vec3(x, y, z) }


private data class Connection(val fromIndex: Int, val toIndex: Int, val distance: Long)

private fun distances(junctions: List<Vec3>): List<Connection> = buildList {

    for (a in junctions.indices)
        for (b in (a + 1)..junctions.lastIndex)
            add(Connection(a, b, magnitude(junctions[a], junctions[b])))
}.sortedBy { it.distance }

fun magnitude(v1: Vec3, v2: Vec3): Long =
    (v2 - v1).let { (x, y, z) -> x.toLong() * x + y.toLong() * y + z.toLong() * z }
