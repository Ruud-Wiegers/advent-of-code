package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import kotlin.math.sign

object Day09 : AdventSolution(2022, 9, "Rope Bridge") {

    override fun solvePartOne(input: String) = solution(input, 1)
    override fun solvePartTwo(input: String) = solution(input, 9)

    private fun solution(input: String, knots: Int) = input
        .lineSequence()
        .map { it.split(' ') }
        .flatMap { (dir, len) -> List(len.toInt()) { dir.toDirection() } }
        .map(Direction::vector)
        .scan(Vec2.origin, Vec2::plus)
        .tailPath(knots)
        .distinct()
        .count()

    private fun String.toDirection() = when (this) {
        "L" -> Direction.LEFT
        "R" -> Direction.RIGHT
        "U" -> Direction.UP
        "D" -> Direction.DOWN
        else -> throw IllegalArgumentException(this)
    }

    private fun Sequence<Vec2>.tailPath(knots: Int) =
        generateSequence(this) { it.scan(Vec2.origin, ::moveTail) }.elementAt(knots)

    private fun moveTail(previousTail: Vec2, head: Vec2): Vec2 {
        val diff = head - previousTail
        val step = Vec2(diff.x.sign, diff.y.sign)
        return if (diff == step) previousTail else previousTail + step
    }
}