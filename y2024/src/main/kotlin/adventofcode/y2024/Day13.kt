package adventofcode.y2024

import adventofcode.io.AdventSolution
import kotlin.math.*

fun main() = Day13.solve()

object Day13 : AdventSolution(2024, 13, "Claw Contraption") {

    override fun solvePartOne(input: String) = parseInput(input)
        .mapNotNull(::solve)
        .filter { (a, b) -> a <= 100 && b <= 100 }
        .sumOf { (a, b) -> 3 * a + b }


    private const val OFFSET = 10_000_000_000_000.0

    override fun solvePartTwo(input: String) = parseInput(input)
        .map { it.copy(target = it.target + Vec2Double(OFFSET, OFFSET)) }
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

private data class Problem(val a: Vec2Double, val b: Vec2Double, val target: Vec2Double)

private fun solve(problem: Problem): Pair<Long, Long>? {

    // First, rotate the entire coordinate system such that one of the buttons is axis-aligned
    // Button a will now only move in the y direction. Therefore, in the rotated coordinates
    // only button b can contribute to the movement in the x direction.
    // calculate the number of times b should be pressed with simple division of the x-components
    val theta = problem.a.atan2()
    val rotatedTarget = problem.target.rotate(theta)
    val rotatedB = problem.b.rotate(theta)
    val bCount = (rotatedTarget.x / rotatedB.x).roundToLong()

    // back in the original coordinates, move the remaining distance by pressing a
    val remainingDistance = problem.target - problem.b * bCount
    val aCount = (remainingDistance.y / problem.a.y).roundToLong()

    // now test the exact result in the original coordinates
    val clawPosition = problem.a * aCount + problem.b * bCount
    return Pair(aCount, bCount).takeIf { clawPosition == problem.target }
}

private data class Vec2Double(val x: Double, val y: Double) {
    operator fun plus(o: Vec2Double) = Vec2Double(x + o.x, y + o.y)
    operator fun minus(o: Vec2Double) = Vec2Double(x - o.x, y - o.y)
    operator fun times(o: Long) = Vec2Double(x * o.toDouble(), y * o.toDouble())

    fun rotate(theta: Double) =
        Vec2Double(cos(theta) * this.x - sin(theta) * this.y, sin(theta) * this.x + cos(theta) * this.y)

    fun atan2() = atan2(x, y)
}