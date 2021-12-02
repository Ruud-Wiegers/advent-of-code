package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.Vec3

object Day02 : AdventSolution(2021, 2, "Dive!")
{
    override fun solvePartOne(input: String) = parse(input)
        .reduce(Vec2::plus)
        .let { (h, d) -> h * d }

    override fun solvePartTwo(input: String) = parse(input)
        .fold(Vec3.origin) { acc, (dPos, dAim) -> acc + Vec3(dPos, dPos * acc.z, dAim) }
        .let { (h, d, _) -> h * d }

    private fun parse(input: String): Sequence<Vec2> = input
        .lineSequence()
        .map {
            val (direction, s) = it.split(' ')
            val delta = s.toInt()
            when (direction)
            {
                "up"      -> Vec2(0, -delta)
                "down"    -> Vec2(0, delta)
                "forward" -> Vec2(delta, 0)
                else      -> throw IllegalArgumentException()
            }
        }
}
