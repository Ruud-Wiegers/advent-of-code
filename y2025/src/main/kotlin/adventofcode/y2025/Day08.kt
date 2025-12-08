package adventofcode.y2025

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec3

fun main() {
    Day08.solve()
}

object Day08 : AdventSolution(2025, 8, "Playground") {

    override fun solvePartOne(input: String): Long = solvePartOne(input, 1000)

    fun solvePartOne(input: String, connections: Int ): Long {
        val junctions = parse(input)

        val distances = distances(junctions).sortedBy { it.second }.take(connections)
        val sets = DisjointUnionSets(junctions.size)

        distances.forEach { sets.union(it.first.first, it.first.second) }

        return junctions.indices.map { sets.findRoot(it) }
            .groupingBy { it }.eachCount()
            .values.sortedDescending()
            .take(3)
            .map(Int::toLong).reduce(Long::times)

    }

    override fun solvePartTwo(input: String): Long {
        val junctions = parse(input)

        val distances = distances(junctions).sortedBy { it.second }
        val sets = DisjointUnionSets(junctions.size)

        distances.forEach {
            sets.union(it.first.first, it.first.second)
            if (sets.countSets() == 1) {
                return junctions[it.first.first].x.toLong() * junctions[it.first.second].x.toLong()
            }
        }
        return -1
    }
}

private fun parse(input: String): List<Vec3> = input
    .lines()
    .map { it.split(',').map { it.toInt() } }
    .map { (x, y, z) -> Vec3(x, y, z) }


private fun distances(junctions: List<Vec3>): List<Pair<Pair<Int, Int>, Long>> {

    fun magnitude(v1: Vec3, v2: Vec3): Long =
        (v2 - v1).let { (x, y, z) -> x.toLong() * x + y.toLong() * y + z.toLong() * z }

    return buildList {
        for (a in junctions.indices) for (b in (a + 1)..junctions.lastIndex)
            add(a to b to magnitude(junctions[a], junctions[b]))
    }
}

private class DisjointUnionSets(n: Int) {
    private val rank: IntArray = IntArray(n)
    private val parent: IntArray = IntArray(n) { it }
    private var numDisjoint = n

    fun findRoot(x: Int): Int {
        if (parent[x] != x)
            parent[x] = findRoot(parent[x])
        return parent[x]
    }

    fun union(x: Int, y: Int) {
        val xRoot = findRoot(x)
        val yRoot = findRoot(y)
        when {
            xRoot == yRoot -> return
            rank[xRoot] < rank[yRoot] -> parent[xRoot] = yRoot
            rank[yRoot] < rank[xRoot] -> parent[yRoot] = xRoot
            else -> {
                parent[yRoot] = xRoot
                rank[xRoot]++
            }
        }
        numDisjoint--
    }

    fun countSets() = numDisjoint
}
