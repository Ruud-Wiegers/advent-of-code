package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.collections.cartesian
import kotlin.math.absoluteValue
import kotlin.math.sqrt

fun main() = Day10.solve()

object Day10 : AdventSolution(2019, 10, "Monitoring Station") {

    override fun solvePartOne(input: String): Int? {
        val asteroids = input.lines().mapIndexed { rownum, row ->
            row.withIndex().filter { it.value == '#' }.map { Point(rownum, it.index) }
        }
                .flatten()

        val counts = asteroids.associateWith { 0 }.toMutableMap()

        asteroids.cartesian(asteroids).cartesian(asteroids)
                .map { (ab, c) -> listOf(ab.first, ab.second, c) }
                .filterNot { (a, b, c) -> a == b || b == c || c == a }
                .filterNot { (a, b, c) -> inBetween(a, b, c) }
                .forEach { counts.merge(it[0], 1, Int::plus) }

        return counts.values.max()
    }

    private fun inBetween(a: Point, b: Point, c: Point): Boolean {

        fun reduce(point: Point) = point / gcd(point.x.absoluteValue, point.y.absoluteValue)
        fun magnitude(p: Point) = sqrt((p.x * p.x + p.y * p.y).toDouble())
        return reduce(b - a) == reduce(c - a) && magnitude(b - a) < magnitude(c - a)

    }

    private tailrec fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

    override fun solvePartTwo(input: String) = 0

    data class Point(val x: Int, val y: Int) {
        operator fun plus(o: Point) = Point(x + o.x, y + o.y)
        operator fun minus(o: Point) = Point(x - o.x, y - o.y)
        operator fun div(o: Int) = Point(x / o, y / o)
    }


}
