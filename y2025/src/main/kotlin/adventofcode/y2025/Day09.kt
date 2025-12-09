package adventofcode.y2025

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors
import kotlin.math.absoluteValue


fun main() {
    Day09.solve()
}

object Day09 : AdventSolution(2025, 9, "Movie Theater") {

    override fun solvePartOne(input: String): Long {
        val grid = input.lines().map {
            val (x, y) = it.split(",")
            Vec2(x.toInt(), y.toInt())
        }

        val squares = sequence {
            for (a in grid.indices)
                for (b in (a + 1)..grid.lastIndex)
                    (grid[a] - grid[b])
                        .let { (x, y) -> ((x.toLong() + 1) * (y + 1)).absoluteValue }
                        .let { yield(it) }

        }

        return squares.max()

    }

    //assumption: If the perimeter touches itself anywhere, it is not relevant for the flood-fill
    override fun solvePartTwo(input: String): Long {

        // read input to vectors
        val corners = input.lines().map {
            val (x, y) = it.split(",")
            Vec2(x.toInt(), y.toInt())
        }

        // rescale
        val xs: Map<Int, Int> = corners.flatMap { it.x..it.x + 1 }.toSortedSet().withIndex().associate { (i, x) -> x to i }
        val ys: Map<Int, Int> = corners.flatMap { it.y..it.y + 1 }.toSortedSet().withIndex().associate { (i, y) -> y to i }
        fun rescale(v: Vec2) = Vec2(xs.getValue(v.x), ys.getValue(v.y))
        val rescaledCorners = corners.map { rescale(it) }

        // calculate perimeter
        val greenPerimeter = (rescaledCorners + rescaledCorners[0]).zipWithNext { v1, v2 ->
            generateSequence(v1) { it + (v2 - v1).sign }.takeWhile { it != v2 }.toList()
        }.flatten().toSet()

        // convert to grid
        val maxX = greenPerimeter.maxOf { it.x }
        val maxY = greenPerimeter.maxOf { it.y }
        val denseScaledGrid = List(maxY + 1) {
            BooleanArray(maxX + 1) { false }
        }

        // draw perimeter
        for (v in greenPerimeter) denseScaledGrid[v.y][v.x] = true

        // floodfill dense grid
        val startLine = denseScaledGrid.indexOfFirst {  it[0] && !it[1] }
        val startPoint = Vec2(1, startLine)
        val open = mutableListOf(startPoint)

        while (open.isNotEmpty()) {
            val c = open.removeLast()
            denseScaledGrid[c.y][c.x] = true
            open += c.neighbors().filter { v -> !denseScaledGrid[v.y][v.x] }
        }

        // sort rectangles by unscaled size
        val rectangles = buildList {
            for (a in corners.indices)
                for (b in (a + 1)..corners.lastIndex)
                    add(Rectangle(corners[a], corners[b]))

            sortByDescending { it.area() }
        }

        // find first rectangle that covers the rescaled grid
        return rectangles.first { (a, b) -> Rectangle(rescale(a), rescale(b)).fits(denseScaledGrid) }.area()
    }
}

private data class Rectangle(val a: Vec2, val b: Vec2) {
    fun area() = (a - b).let { (x, y) -> ((x.absoluteValue + 1L) * (y.absoluteValue + 1L)) }

    fun fits(grid: List<BooleanArray>): Boolean {
        val (x1, x2) = listOf(a.x, b.x).sorted()
        val (y1, y2) = listOf(a.y, b.y).sorted()
        for (y in y1..y2)
            for (x in x1..x2)
                if (!grid[y][x]) return false

        return true
    }
}
