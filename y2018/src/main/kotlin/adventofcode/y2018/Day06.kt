package adventofcode.y2018

import adventofcode.AdventSolution
import kotlin.math.abs

object Day06 : AdventSolution(2018, 6, "Chronal Coordinates") {

    override fun solvePartOne(input: String): Int? {
        val points = parse(input)
        val counts = mutableMapOf<Point, Int>()

        val disqualified = mutableSetOf<Point>()

        val width = points.width()
        val height = points.height()

        width.forEach { x ->
            height.forEach { y ->

                //add the point to the region whose origin is strictly closest
                val closest = findUniqueClosest(points, x, y)
                closest?.let {
                    counts[it] = (counts[it] ?: 0) + 1

                    //any region that touches an edge stretches out to infinity and is disqualified
                    if (x == width.first || x == width.last || y == height.first || y == height.last)
                        disqualified += it
                }
            }
        }

        return counts.filterKeys { it !in disqualified }.values.maxOrNull()

    }

    private fun findUniqueClosest(points: List<Point>, x: Int, y: Int): Point? {
        var closestPoint: Point? = null
        var dist = Int.MAX_VALUE

        for (p in points) {
            val d = p.distanceTo(x, y)
            if (d == dist) closestPoint = null
            if (d < dist) {
                closestPoint = p
                dist = d
            }
        }
        return closestPoint
    }

    override fun solvePartTwo(input: String): Int {
        val points = parse(input)

        val height = points.height()
        return points.width().sumBy { x ->
            height.asSequence()
                    .map { y ->
                        points.sumBy { it.distanceTo(x, y) }
                    }
                    .count { it < 10000 }
        }
    }

    private fun parse(input: String) = input.lineSequence()
            .map { it.split(", ") }
            .map { (x, y) -> Point(x.toInt(), y.toInt()) }
            .toList()

    private data class Point(val x: Int, val y: Int) {
        fun distanceTo(x: Int, y: Int) = abs(this.x - x) + abs(this.y - y)
    }

    private fun List<Point>.width() = minOf { it.x }..maxOf { it.x }
    private fun List<Point>.height() = minOf { it.y }..maxOf { it.y }
}
