package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Direction.*
import kotlin.math.absoluteValue

fun main() {
    Day18.solve()
}

object Day18 : AdventSolution(2023, 18, "Lavaduct Lagoon") {

    override fun solvePartOne(input: String) = parse(input).let(::area)
    override fun solvePartTwo(input: String) = parseHex(input).let(::area)


    private fun area(lines: List<Pair<Direction, Long>>): Long {
        var x = 0L
        var area = 0L

        for ((direction, length) in lines) {
            when (direction) {
                UP -> area += x * length
                DOWN -> area -= x * length
                RIGHT -> x += length
                LEFT -> x -= length
            }
        }

        val halfPerimeter = lines.sumOf { it.second } / 2L + 1L
        return area.absoluteValue + halfPerimeter
    }
}


private fun parse(input: String) = input.lines().map {
    val (dir, len) = it.split(" ")

    val direction = when (dir) {
        "R" -> RIGHT
        "D" -> DOWN
        "L" -> LEFT
        "U" -> UP
        else -> error("direction")
    }

    Pair(direction, len.toLong())
}

private fun parseHex(input: String) = input.lines().map {
    val dir = it.takeLast(2).first()
    val len = it.takeLast(7).dropLast(2)

    val direction = when (dir) {
        '0' -> RIGHT
        '1' -> DOWN
        '2' -> LEFT
        '3' -> UP
        else -> error("direction")
    }

    Pair(direction, len.toLong(16))
}