package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve
import kotlin.math.abs

fun main() = Day10.solve()

object Day10 : AdventSolution(2018, 10, "The Stars Align") {

    override fun solvePartOne(input: String) = parse(input).findMessage().value.map(Light::p).toList().asMessage()

    override fun solvePartTwo(input: String) = parse(input).findMessage().index

    private fun List<Light>.findMessage(): IndexedValue<List<Light>> {
        return generateSequence(this) { it.map(Light::next) }
                .withIndex().dropWhile { it.value.any { !it.isVisible() } }
                .first { it.value.all { a -> it.value.any { other -> a.adjacent(other) } } }
    }

    private fun parse(input: String) = input
            .splitToSequence("\n")
            .map {
                it
                        .split("position=<", ",", "> velocity=<", ">")
                        .map(String::trim)
                        .filter(String::isNotBlank)
                        .map(String::toInt)
            }
            .map { (px, py, vx, vy) -> Light(px, py, vx, vy) }
            .toList()

    private fun List<Point>.asMessage(): String {
        val x0 = minBy { it.x }!!.x
        val x1 = maxBy { it.x }!!.x
        val y0 = minBy { it.y }!!.y
        val y1 = maxBy { it.y }!!.y

        val grid = List(y1 - y0 + 1) { CharArray(x1 - x0 + 1) { ' ' } }

        forEach { (x, y) -> grid[y - y0][x - x0] = '#' }

        return grid.joinToString("") { "\n" + String(it) }
    }
}

private data class Light(val p: Point, val v: Point) {
    constructor(px: Int, py: Int, vx: Int, vy: Int) : this(Point(px, py), Point(vx, vy))

    fun next() = Light(p + v, v)
    fun isVisible() = p.x >= 0 && p.y >= 0
    fun adjacent(o: Light) = abs(p.x - o.p.x) + abs(p.y - o.p.y) in 1..2
}

private data class Point(val x: Int, val y: Int) {
    operator fun plus(o: Point) = Point(x + o.x, y + o.y)
}
