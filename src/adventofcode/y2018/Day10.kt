package adventofcode.y2018

import adventofcode.AdventSolution
import kotlin.math.abs

object Day10 : AdventSolution(2018, 10, "The Stars Align") {

    override fun solvePartOne(input: String) = parse(input).findMessage().value.asMessage()

    override fun solvePartTwo(input: String) = parse(input).findMessage().index

    private fun parse(input: String) =
            input.lineSequence()
                    .map {
                        it
                                .split("position=<", ",", "> velocity=<", ">")
                                .map(String::trim)
                                .filter(String::isNotEmpty)
                                .map(String::toInt)
                    }
                    .map { (px, py, vx, vy) -> Light(px, py, vx, vy) }
                    .toList()
                    .let(::Sky)

    private fun Sky.findMessage(): IndexedValue<Sky> =
            generateSequence(this, Sky::nextSecond)
                    .withIndex()
                    .dropWhile { it.value.isVisible() }
                    .first { it.value.isAMessage() }


    private data class Sky(val lights: List<Light>) {
        val width: IntRange by lazy { lights.minBy { it.p.x }!!.p.x..lights.maxBy { it.p.x }!!.p.x }
        val height: IntRange by lazy { lights.minBy { it.p.y }!!.p.y..lights.maxBy { it.p.y }!!.p.y }

        fun nextSecond() = Sky(lights.map(Light::nextSecond))

        fun isVisible() = width.first >= 0 && height.first >= 0
        fun isAMessage() = lights.all { light -> lights.any { other -> light.adjacent(other) } }

        fun asMessage(): String {
            val grid = List(height.count()) { CharArray(width.count()).apply { fill(' ') } }

            lights.forEach { (p, _) -> grid[p.y - height.first][p.x - width.first] = '█' }

            return grid.joinToString("") { "\n" + String(it) }
        }
    }

    private data class Light(val p: Point, val v: Point) {
        constructor(px: Int, py: Int, vx: Int, vy: Int) : this(Point(px, py), Point(vx, vy))

        fun nextSecond() = Light(p + v, v)
        fun adjacent(o: Light) = abs(p.x - o.p.x) + abs(p.y - o.p.y) in 1..2
    }

    private data class Point(val x: Int, val y: Int) {
        operator fun plus(o: Point) = Point(x + o.x, y + o.y)
    }
}