package adventofcode.y2024

import adventofcode.io.AdventSolution
import java.awt.geom.Point2D
import kotlin.math.*

fun main() = Day13.solve()

object Day13 : AdventSolution(2024, 13, "Claw Contraption") {

    override fun solvePartOne(input: String) = parseInput(input)
        .mapNotNull(::solve)
        .filter { (a, b) -> a <= 100 && b <= 100 }
        .sumOf { (a, b) -> 3 * a + b }


    override fun solvePartTwo(input: String) = parseInput(input)
        .map { it.copy(t = Point2D.Double(10000000000000.0 + it.t.x, 10000000000000.0 + it.t.y)) }
        .mapNotNull(::solve)
        .sumOf { (a, b) -> 3 * a + b }

}

private fun parseInput(input: String): List<Problem> = input.split("\n\n")
    .map {
        val ds = """\d+""".toRegex()
            .findAll(it)
            .toList()
            .map { it.value.toDouble() }
            .chunked(2)
            .map { (x, y) -> Point2D.Double(x, y) }

        Problem(ds[0], ds[1], ds[2])
    }

private data class Problem(val a: Point2D, val b: Point2D, val t: Point2D) {
    fun rotate(theta: Double) = Problem(a.rotate(theta), b.rotate(theta), t.rotate(theta))
}

private fun Point2D.rotate(theta: Double) =
    Point2D.Double(cos(theta) * this.x - sin(theta) * this.y, sin(theta) * this.x + cos(theta) * this.y)


private fun solve(problem: Problem): Pair<Long, Long>? {
    val theta = atan2(problem.a.x, problem.a.y)
    val rotated = problem.rotate(theta)

    val nB = rotated.t.x / rotated.b.x
    val nA = (rotated.t.y - nB * rotated.b.y) / rotated.a.y

    if (!nA.isInteger()) return null
    if (!nB.isInteger()) return null
    return Pair(nA.absoluteValue.roundToLong(), nB.absoluteValue.roundToLong())
}

private fun Double.isInteger() = (this - round(this)).absoluteValue < 0.001