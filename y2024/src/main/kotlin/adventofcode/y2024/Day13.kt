package adventofcode.y2024

import adventofcode.io.AdventSolution
import kotlin.math.*

fun main() = Day13.solve()

object Day13 : AdventSolution(2024, 13, "Claw Contraption") {

    override fun solvePartOne(input: String) = parseInput(input)
        .mapNotNull(::solve)
        .filter { (a, b) -> a <= 100 && b <= 100 }
        .sumOf { (a, b) -> 3 * a + b }


    private val offset = 10_000_000_000_000.0

    override fun solvePartTwo(input: String) = parseInput(input)
        .map { it.copy(t = it.t + Vec2Double(offset, offset)) }
        .mapNotNull(::solve)
        .sumOf { (a, b) -> 3 * a + b }

}

private fun parseInput(input: String): List<Problem> = input.split("\n\n")
    .map {
        val (a, b, target) = """\d+""".toRegex().findAll(it).toList()
            .map { it.value.toDouble() }
            .chunked(2)
            .map { (x, y) -> Vec2Double(x, y) }

        Problem(a, b, target)
    }

private data class Problem(val a: Vec2Double, val b: Vec2Double, val t: Vec2Double) {
    fun rotate(theta: Double) = Problem(a.rotate(theta), b.rotate(theta), t.rotate(theta))
}

private fun solve(problem: Problem): Pair<Long, Long>? {

    // First, rotate the entire coordinate system such that one of the buttons is axis-aligned
    // Button a will now only move in the y direction
    val theta = problem.a.atan2()
    val rotated = problem.rotate(theta)

    // Button b is the only button that can change the x position now, so figure that out first
    // in the rotated coordinates
    val bCount = (rotated.t.x / rotated.b.x).roundToLong()

    // back in the original coordinates, move the remaining distance by pressing a
    val remainingDistance = problem.t - problem.b * bCount
    val aCount = (remainingDistance.y / problem.a.y).roundToLong()

    //now test the calculation in the original coordinates
    return Pair(aCount, bCount).takeIf { problem.a * aCount + problem.b * bCount == problem.t }
}

private data class Vec2Double(val x: Double, val y: Double) {
    operator fun plus(o: Vec2Double) = Vec2Double(x + o.x, y + o.y)
    operator fun minus(o: Vec2Double) = Vec2Double(x - o.x, y - o.y)
    operator fun div(o: Long) = Vec2Double(x / o.toDouble(), y / o.toDouble())
    operator fun times(o: Long) = Vec2Double(x * o.toDouble(), y * o.toDouble())

    fun rotate(theta: Double) =
        Vec2Double(cos(theta) * this.x - sin(theta) * this.y, sin(theta) * this.x + cos(theta) * this.y)

    fun atan2() = atan2(x, y)
}