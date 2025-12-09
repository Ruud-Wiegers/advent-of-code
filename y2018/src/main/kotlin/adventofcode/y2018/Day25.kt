package adventofcode.y2018

import adventofcode.io.AdventSolution
import adventofcode.util.algorithm.UnionFind
import kotlin.math.abs


fun main() = Day25.solve()

object Day25 : AdventSolution(2018, 25, "Four-Dimensional Adventure") {

    override fun solvePartOne(input: String): Int {
        val constellations = input.splitToSequence('\n', ',')
                .map(String::toInt)
                .chunked(4)
                .toList()

        val mergeFind = UnionFind(constellations.size)
        for (a in constellations.indices)
            for (b in 0 until a)
                if (distance(constellations[a], constellations[b]) <= 3)
                    mergeFind.union(a, b)

        return mergeFind.setCount
    }


    override fun solvePartTwo(input: String) = "Free Star! ^_^"
}

private fun distance(xs: List<Int>, ys: List<Int>) = xs.zip(ys) { x, y -> abs(x - y) }.sum()
