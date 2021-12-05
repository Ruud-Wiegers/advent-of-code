package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.util.vector.Vec2
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

object Day05 : AdventSolution(2021, 5, "Hydrothermal Venture")
{
    override fun solvePartOne(input: String) =
        parseInput(input)
            .filter { (a, b) -> a.x == b.x || a.y == b.y }
            .countOverlap()

    override fun solvePartTwo(input: String) = parseInput(input).countOverlap()

    private fun Sequence<Line>.countOverlap() =
        flatMap(Line::toPointSequence)
            .groupingBy { it }
            .eachCount()
            .count { it.value > 1 }

    private data class Line(val a: Vec2, val b: Vec2)
    {
        fun toPointSequence(): Sequence<Vec2>
        {
            val delta = (b - a)
            val step = Vec2(delta.x.sign, delta.y.sign)
            val length = max(delta.x.absoluteValue, delta.y.absoluteValue) + 1
            return generateSequence(a, step::plus).take(length)
        }
    }

    private fun parseInput(input: String): Sequence<Line>
    {
        val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
        return input.lineSequence()
            .map { regex.matchEntire(it)!!.destructured }
            .map { (x1, y1, x2, y2) ->
                Line(Vec2(x1.toInt(), y1.toInt()), Vec2(x2.toInt(), y2.toInt()))
            }
    }
}
