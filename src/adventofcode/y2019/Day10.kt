package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import kotlin.math.absoluteValue

fun main() = Day10.solve()

object Day10 : AdventSolution(2019, 10, "Monitoring Station") {

    override fun solvePartOne(input: String) = losPerAsteroid(parseToAsteroids(input.lines())).values.max()

    override fun solvePartTwo(input: String): Int {
        val asteroids = parseToAsteroids(input.lines())
        val station = losPerAsteroid(asteroids).maxBy { it.value }!!.key

        val vectorsToOtherAsteroids = asteroids
                .filter { it != station }
                .filter { lineOfSight(station, it, asteroids) }
                .map { it - station }
                .sortedBy { it.y.toDouble() / it.x.toDouble() }
                .sortedBy { it.x < 0 }

        val targetAsteroid = vectorsToOtherAsteroids[199] + station
        return targetAsteroid.x * 100 + targetAsteroid.y
    }

    private fun parseToAsteroids(grid: List<String>): Set<Point> = sequence {
        for (y in grid.indices)
            for (x in grid[0].indices)
                if (grid[y][x] == '#')
                    yield(Point(x, y))
    }.toSet()

    private fun losPerAsteroid(asteroids: Set<Point>) = asteroids.associateWith { k ->
        asteroids.filter { it != k }.count { other ->
            lineOfSight(k, other, asteroids)
        }
    }

    private fun lineOfSight(from: Point, to: Point, blockingPoints: Set<Point>): Boolean {
        val step = (from - to).reducedAngle()
        return generateSequence(from - step) { it - step }
                .takeWhile { it != to }
                .none { it in blockingPoints }
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(o: Point) = Point(x + o.x, y + o.y)
        operator fun minus(o: Point) = Point(x - o.x, y - o.y)
        operator fun div(o: Int) = Point(x / o, y / o)
        fun reducedAngle() = this / gcd(x.absoluteValue, y.absoluteValue)
    }

    private tailrec fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
}
