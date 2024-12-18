package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors
import adventofcode.util.vector.xBounds
import adventofcode.util.vector.yBounds
import kotlin.math.abs

fun main() {
    Day18.solve()
}

object Day18 : AdventSolution(2024, 18, "RAM Run") {

    override fun solvePartOne(input: String): Int {
        val list = input.parseInput()

        val bounds = Box(list.xBounds(), list.yBounds())
        val walls = list.take(1024).toSet()

        var open = setOf(Vec2(0, 0))
        var count = 0
        val seen = open.toMutableSet()
        val end = Vec2(bounds.xBounds.last, bounds.yBounds.last)
        while (end !in open) {
            val candidates = open.flatMap(Vec2::neighbors).filter(bounds::contains).filterNot(seen::contains)
                .filterNot(walls::contains).toSet()
            seen += candidates
            open = candidates
            count++
        }

        return count

    }

    override fun solvePartTwo(input: String): String {
        val list = input.parseInput()

        val bounds = Box(list.xBounds(), list.yBounds())

        repeat(list.size) {
            val walls = list.take(it).toSet()
            if (!canEscape(bounds, walls)) return list[it-1].let { (x, y) -> "$x,$y" }
        }
        return ""
    }

    private fun canEscape(bounds: Box, walls: Set<Vec2>): Boolean {
        var open = setOf(Vec2(0, 0))
        var count = 0
        val seen = open.toMutableSet()
        val end = Vec2(bounds.xBounds.last, bounds.yBounds.last)
        while (end !in open && open.isNotEmpty()) {
            val candidates = open.flatMap(Vec2::neighbors).filter(bounds::contains).filterNot(seen::contains)
                .filterNot(walls::contains).toSet()
            seen += candidates
            open = candidates
            count++
        }

        return open.isNotEmpty()
    }

    fun String.parseInput(): List<Vec2> = lines().map {
        it.split(",").map(String::toInt).let { (x, y) -> Vec2(x, y) }
    }

    private data class Box(val xBounds: IntRange, val yBounds: IntRange) {
        operator fun contains(v: Vec2) = v.x in xBounds && v.y in yBounds
    }
}

