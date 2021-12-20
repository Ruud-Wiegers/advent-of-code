package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.util.vector.Vec2

object Day17 : AdventSolution(2021, 17, "Trick Shot") {
    override fun solvePartOne(input: String): Int {
        val (_,yT) = parse(input)
        return yT.first * (yT.first+1) / 2
    }

    override fun solvePartTwo(input: String): Int {
        val (xT,yT) = parse(input)


        val dx = 0..xT.last
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

    fun parse(input: String): Pair<IntRange, IntRange> {
        val (x0,x1,y0,y1) = """target area: x=(-?\d+)\.\.(-?\d+), y=(-?\d+)\.\.(-?\d+)""".toRegex().matchEntire(input)!!.destructured

        return x0.toInt()..x1.toInt() to y0.toInt()..y1.toInt()
    }
}
