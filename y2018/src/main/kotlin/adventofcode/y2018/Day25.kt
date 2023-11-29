package adventofcode.y2018

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import kotlin.math.abs


fun main() = Day25.solve()

object Day25 : AdventSolution(2018, 25, "Four-Dimensional Adventure") {

    override fun solvePartOne(input: String): Int {
        val constellations = input.splitToSequence('\n', ',')
                .map(String::toInt)
                .chunked(4)
                .toList()

        val mergeFind = DisjointUnionSets(constellations.size)
        for (a in constellations.indices)
            for (b in 0 until a)
                if (distance(constellations[a], constellations[b]) <= 3)
                    mergeFind.union(a, b)

        return mergeFind.countSets()
    }


    override fun solvePartTwo(input: String) = "Free Star! ^_^"
}

private fun distance(xs: List<Int>, ys: List<Int>) = xs.zip(ys) { x, y -> abs(x - y) }.sum()

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
