package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors
import adventofcode.util.vector.xBounds
import adventofcode.util.vector.yBounds

fun main() {
    Day18.solve()
}

object Day18 : AdventSolution(2024, 18, "RAM Run") {

    override fun solvePartOne(input: String): Int {
        val list = input.parseInput()

        val bounds = Box(list.xBounds(), list.yBounds())
        val walls = list.take(1024).toSet()

        var count = 0
        var open = setOf(Vec2(0, 0))
        val closed = open.toMutableSet()
        val end = Vec2(bounds.xBounds.last, bounds.yBounds.last)
        while (end !in open) {
            val candidates = open.flatMap(Vec2::neighbors).filter(bounds::contains).filterNot(closed::contains)
                .filterNot(walls::contains).toSet()
            closed += candidates
            open = candidates
            count++
        }

        return count

    }

    override fun solvePartTwo(input: String): String {
        val list = input.parseInput()
        val bounds = Box(list.xBounds(), list.yBounds())
        return binarySearch(bounds, list)
    }

}

private fun String.parseInput(): List<Vec2> = lines().map {
    it.split(",").map(String::toInt).let { (x, y) -> Vec2(x, y) }
}

private fun binarySearch(bounds: Box, list: List<Vec2>): String {
    var low = 0
    var high = list.lastIndex
    while (high - low > 1) {
        val middle = low + (high - low) / 2
        val blocked = list.take(middle).toSet()
        if (canEscape(bounds, blocked))
            low = middle
        else
            high = middle
    }

    return list[low].let { (x, y) -> "$x,$y" }
}

private fun canEscape(bounds: Box, walls: Set<Vec2>): Boolean {
    val start = Vec2(0, 0)
    val goal = Vec2(bounds.xBounds.last, bounds.yBounds.last)
    var open = setOf(start)
    val closed = open.toMutableSet()
    while (goal !in open && open.isNotEmpty()) {
        val candidates = open.flatMap(Vec2::neighbors).filter(bounds::contains).filterNot(closed::contains)
            .filterNot(walls::contains).toSet()
        closed += candidates
        open = candidates
    }

    return open.isNotEmpty()
}

private data class Box(val xBounds: IntRange, val yBounds: IntRange) {
    operator fun contains(v: Vec2) = v.x in xBounds && v.y in yBounds
}

