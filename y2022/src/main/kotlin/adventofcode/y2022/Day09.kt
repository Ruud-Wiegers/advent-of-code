package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import kotlin.math.sign


object Day09 : AdventSolution(2022, 9, "Rope Bridge") {

    override fun solvePartOne(input: String) = input.lineSequence()
        .flatMap {
            val (dir, len) = it.split(' ')
            List(len.toInt()) { dir.toDirection() }
        }
        .map { it.vector }
        .scan(Vec2.origin, Vec2::plus)
        .scan(Vec2.origin, ::tailMove)
        .distinct()
        .count()


    override fun solvePartTwo(input: String) = input.lineSequence()
        .flatMap {
            val (dir, len) = it.split(' ')
            List(len.toInt()) { dir.toDirection() }
        }
        .map(Direction::vector)
        .scan(Vec2.origin, Vec2::plus)
        .scan(Vec2.origin, ::tailMove)
        .scan(Vec2.origin, ::tailMove)
        .scan(Vec2.origin, ::tailMove)
        .scan(Vec2.origin, ::tailMove)
        .scan(Vec2.origin, ::tailMove)
        .scan(Vec2.origin, ::tailMove)
        .scan(Vec2.origin, ::tailMove)
        .scan(Vec2.origin, ::tailMove)
        .scan(Vec2.origin, ::tailMove)
        .distinct()
        .count()

    private fun String.toDirection() = when (this) {
        "L" -> Direction.LEFT
        "R" -> Direction.RIGHT
        "U" -> Direction.UP
        "D" -> Direction.DOWN
        else -> throw IllegalArgumentException(this)
    }

    private fun tailMove(prev: Vec2, head: Vec2): Vec2 {
        val diff = head - prev
        val step = Vec2(diff.x.sign, diff.y.sign)
        return if (diff == step) prev else prev + step
    }
}

