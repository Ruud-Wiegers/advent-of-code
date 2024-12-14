package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors

fun main() = Day14.solve()

object Day14 : AdventSolution(2024, 14, "Restroom Redoubt") {

    override fun solvePartOne(input: String): Long {

        val x = 101
        val y = 103

        val positions = parseInput(input)
            .map { it.copy(v = Vec2((it.v.x + x) % x, (it.v.y + y) % y)) }
            .map { it.move(100) }
            .map { Vec2(it.p.x % x, it.p.y % y) }

        val bounds = bounds(x, y)

        return bounds.map { quadrant -> positions.count { it in quadrant }.toLong() }.reduce(Long::times)
    }


    override fun solvePartTwo(input: String): Int {

        val initial = parseInput(input).map { it.copy(v = Vec2((it.v.x + 101) % 101, (it.v.y + 103) % 103)) }

        return generateSequence(initial) {
            it.map {
                it.copy(p = (it.p + it.v).let { (x, y) -> Vec2(x % 101, y % 103) })
            }
        }
            .map { it.map { it.p }.toSet() }
            .indexOfFirst { set -> set.count { it.neighbors().all(set::contains) } > 10 }
    }
}

private fun parseInput(input: String): List<Particle> = input.lines()
    .map {
        val (px, py, vx, vy) = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex()
            .matchEntire(it)!!.groupValues.drop(1).map(String::toInt)

        Particle(Vec2(px, py), Vec2(vx, vy))
    }

private fun bounds(xMax: Int, yMax: Int): List<Box> {
    val xMid = xMax / 2
    val yMid = yMax / 2

    return listOf(
        Box(0 until xMid, 0 until yMid),
        Box(0 until xMid, yMid + 1 until yMax),
        Box(xMid + 1 until xMax, 0 until yMid),
        Box(xMid + 1 until xMax, yMid + 1 until yMax),
    )
}

private data class Particle(val p: Vec2, val v: Vec2) {
    fun move(tick: Int) = copy(p = p + v * tick)
}

private data class Box(val xBounds: IntRange, val yBounds: IntRange) {
    operator fun contains(v: Vec2) = v.x in xBounds && v.y in yBounds
}
