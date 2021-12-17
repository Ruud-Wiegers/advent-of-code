package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.util.vector.Vec2

object Day17 : AdventSolution(2021, 17, "???") {
    override fun solvePartOne(input: String) = 92 * 93 / 2

    override fun solvePartTwo(input: String): Int {
        val xT = 195..238
        val yT = -93..-67

        val dx = 20..xT.last
        val dy = yT.first until -yT.first

        return dy.flatMap { y -> dx.map { x -> Vec2(x, y) } }
            .count { velocity ->
                generateSequence(Vec2(0, 0) to velocity) { (p, v) ->
                    (p + v) to Vec2(if (v.x > 0) v.x - 1 else v.x, v.y - 1)
                }
                    .takeWhile { (p, _) -> p.x <= xT.last && p.y >= yT.first }
                    .any { (p, _) -> p.x in xT && p.y in yT }
            }
    }
}
