package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main()
{
    Day05.solve()
}

object Day05 : AdventSolution(2021, 5, "Hydrothermal Venture")
{
    override fun solvePartOne(input: String) = parseInput(input)
        .flatMap { (a, b) ->
            when
            {
                a.x == b.x -> (min(a.y, b.y)..max(a.y, b.y)).map { Vec2(a.x, it) }
                a.y == b.y -> (min(a.x, b.x)..max(a.x, b.x)).map { Vec2(it, a.y) }
                else       -> emptyList()
            }
        }
        .groupingBy { it }
        .eachCount()
        .count { it.value > 1 }

    override fun solvePartTwo(input: String) = parseInput(input)
        .flatMap { (a, b) ->
            val delta = (b - a)
            val sign = Vec2(delta.x.sign, delta.y.sign)
            val length = max(delta.x.absoluteValue, delta.y.absoluteValue) + 1
            generateSequence(a) { it + sign }.take(length)
        }
        .groupingBy { it }
        .eachCount()
        .count { it.value > 1 }

    private fun parseInput(input: String): Sequence<Area>
    {
        val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
        return input.lineSequence()
            .map { regex.matchEntire(it)!!.destructured }
            .map { (x1, y1, x2, y2) ->
                Area(Vec2(x1.toInt(), y1.toInt()), Vec2(x2.toInt(), y2.toInt()))
            }
    }

    private data class Area(val a: Vec2, val b: Vec2)
}
