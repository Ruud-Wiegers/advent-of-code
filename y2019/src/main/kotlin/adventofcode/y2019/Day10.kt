package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.vector.Vec2
import kotlin.math.absoluteValue

fun main() = Day10.solve()

object Day10 : AdventSolution(2019, 10, "Monitoring Station") {

    override fun solvePartOne(input: String) = losPerAsteroid(parseToAsteroids(input.lines())).values.maxOrNull()

    override fun solvePartTwo(input: String): Int {
        val asteroids = parseToAsteroids(input.lines())
        val station = losPerAsteroid(asteroids).maxByOrNull { it.value }!!.key

        val vectorsToOtherAsteroids = asteroids
            .asSequence()
            .filter { it != station }
            .filter { lineOfSight(station, it, asteroids) }
            .map { it - station }
            .sortedBy { it.y.toDouble() / it.x.toDouble() }
            .sortedBy { it.x < 0 }
            .toList()

        val targetAsteroid = vectorsToOtherAsteroids[199] + station
        return targetAsteroid.x * 100 + targetAsteroid.y
    }

    private fun parseToAsteroids(grid: List<String>): Set<Vec2> = sequence {
        for (y in grid.indices)
            for (x in grid[0].indices)
                if (grid[y][x] == '#')
                    yield(Vec2(x, y))
    }.toSet()

    private fun losPerAsteroid(asteroids: Set<Vec2>) = asteroids.associateWith { k ->
        asteroids.filter { it != k }.count { other ->
            lineOfSight(k, other, asteroids)
        }
    }

    private fun lineOfSight(from: Vec2, to: Vec2, blockingPoints: Set<Vec2>): Boolean {
        val step = (from - to).reducedAngle()
        return generateSequence(from - step) { it - step }
            .takeWhile { it != to }
            .none { it in blockingPoints }
    }

    private fun Vec2.reducedAngle() = this / gcd(x.absoluteValue, y.absoluteValue)

    private tailrec fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
}
