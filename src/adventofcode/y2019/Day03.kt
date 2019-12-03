package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.scan
import kotlin.math.absoluteValue

fun main() = Day03.solve()

object Day03 : AdventSolution(2019, 3, "Crossed Wires") {

    override fun solvePartOne(input: String): Int? {
        val (a, b) = input.lines()

        val visited = parseWire(a).toSet()

        return parseWire(b)
                .filter { it in visited }
                .map { it.x.absoluteValue + it.y.absoluteValue }
                .min()
    }

    override fun solvePartTwo(input: String): Int? {
        val (a, b) = input.lines()

        val visited = mutableMapOf<Point, Int>()
        parseWire(a).forEachIndexed { d, p -> visited.putIfAbsent(p, d) }

        return parseWire(b)
                .withIndex()
                .filter { it.value in visited }
                .map { it.index + visited.getValue(it.value) + 2 }
                .min()
    }

    private fun parseWire(a: String) = a.splitToSequence(',')
            .flatMap {
                val dir = parseDirection(it[0])
                val dist = it.drop(1).toInt()
                generateSequence { dir }.take(dist)
            }
            .scan(Point(0, 0), Point::plus)


    private fun parseDirection(ch: Char): Point = when (ch) {
        'U' -> Point(0, -1)
        'D' -> Point(0, 1)
        'L' -> Point(-1, 0)
        'R' -> Point(1, 0)
        else -> throw IllegalStateException()
    }
}


private data class Point(val x: Int, val y: Int) {
    operator fun plus(o: Point) = Point(x + o.x, y + o.y)
}