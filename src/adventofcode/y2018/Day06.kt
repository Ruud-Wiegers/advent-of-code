package adventofcode.y2018

import adventofcode.AdventSolution
import kotlin.math.abs

object Day06 : AdventSolution(2018, 6, "Chronal Coordinates") {

    override fun solvePartOne(input: String): String {
        val points = parse(input)

        val counts = points.associateWith { 0 }.toMutableMap()

        //edges
        val x0 = points.map { it.first }.min()!!
        val x1 = points.map { it.first }.max()!!
        val y0 = points.map { it.second }.min()!!
        val y1 = points.map { it.second }.max()!!

        //all points whose regions go infinite
        val disqualified = mutableSetOf<Pair<Int, Int>>()

        for (x in x0..x1) {
            for (y in y0..y1) {
                //add the point to the region whose origin is strictly closest
                val ds = points.groupBy { it.distance(x, y) }.toSortedMap()
                val closest = ds[ds.firstKey()] ?: emptyList()
                if (closest.size == 1) counts[closest[0]] = counts[closest[0]]!! + 1

                //any region that touches an edge stretches out to infinity and is disqualified
                if (x == x0 || x == x1 || y == y0 || y == y1) disqualified += closest[0]
            }
        }

        return counts.filterKeys { it !in disqualified }.values.max().toString()

    }

    override fun solvePartTwo(input: String): String {
        val points = parse(input)


        val x0 = points.map { it.first }.min()!!
        val x1 = points.map { it.first }.max()!!
        val y0 = points.map { it.second }.min()!!
        val y1 = points.map { it.second }.max()!!

        val count = (x0..x1).sumBy { x ->
            (y0..y1).asSequence()
                    .map { y ->
                        points.sumBy { it.distance(x, y) }
                    }
                    .count { it < 10000 }
        }

        return count.toString()
    }

    private fun parse(input: String): List<Pair<Int, Int>> {
        val points = input.splitToSequence("\n")
                .map { it.substringBefore(',').toInt() to it.substringAfter(' ').toInt() }
                .toList()
        return points
    }

    private fun Pair<Int, Int>.distance(x: Int, y: Int) =
            abs(x - first) + abs(y - second)

}
