package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.xBounds
import adventofcode.util.vector.yBounds

fun main() {
    Day08.solve()
}

object Day08 : AdventSolution(2024, 8, "Resonant Collinearity") {

    override fun solvePartOne(input: String): Int {
        val grid = parseInput(input)
        val bounds = Bounds(grid.values.flatten())
        val frequencies = grid - '.'

        return frequencies.flatMap { findAntiNodes(it.value) }
            .filter { it in bounds }
            .toSet()
            .count()
    }

    override fun solvePartTwo(input: String): Int {
        val grid = parseInput(input)
        val bounds = Bounds(grid.values.flatten())
        val frequencies = grid - '.'

        return frequencies.flatMap { findAntiNodesTwo(it.value, bounds) }
            .filter { it in bounds }
            .toSet()
            .count()
    }
}

private fun findAntiNodes(nodes: List<Vec2>): List<Vec2> = combinations(nodes) { a, b -> a + a - b }

private fun findAntiNodesTwo(nodes: List<Vec2>, bounds: Bounds): List<Vec2> = combinations(nodes) { a, b ->
    val step = a - b
    generateSequence(a, step::plus).takeWhile(bounds::contains).toList()
}.flatten()


private fun parseInput(input: String): Map<Char, List<Vec2>> = input.lines().flatMapIndexed { y, line ->
    line.mapIndexed { x, c -> c to Vec2(x, y) }
}.groupBy(Pair<Char, Vec2>::first, Pair<Char, Vec2>::second)

private data class Bounds(val xBounds: IntRange, val yBounds: IntRange) {
    constructor(grid: Collection<Vec2>) : this(grid.xBounds(), grid.yBounds())

    operator fun contains(v: Vec2) = v.x in xBounds && v.y in yBounds


}



inline fun <T, R> combinations(items: Iterable<T>, crossinline transform: (T, T) -> R): List<R> = buildList {
    for (a in items) {
        for (b in items) {
            if (a == b) continue

            add(transform(a, b))
        }
    }
}
