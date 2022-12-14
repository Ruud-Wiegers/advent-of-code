package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2
import kotlin.math.sign


fun main() {
    Day14.solve()
}

object Day14 : AdventSolution(2022, 14, "Distress Signal") {

    override fun solvePartOne(input: String): Int {

        val walls: Set<Vec2> = parse(input).toSet()
        val covered = walls.toMutableSet()
        val edge = walls.maxOf { it.y }

        do {
            val grain = generateSequence(Vec2(500, 0)) { it.fall().find { it !in covered } }
                .takeWhile { it.y <= edge }
                .last()
            covered += grain
        } while (grain.y < edge)
        return covered.size - walls.size - 1
    }

    override fun solvePartTwo(input: String): Int {
        val walls = parse(input).groupBy(Vec2::y).mapValues { it.value.mapTo(mutableSetOf(), Vec2::x) }

        return (1..walls.keys.max() + 1)
            .scan(setOf(500)) { prevLine, y ->
                val currentLine = (-1..1).flatMapTo(mutableSetOf()) { dx -> prevLine.map(dx::plus) }
                val currentWalls = walls.getOrDefault(y, emptySet())
                currentLine - currentWalls
            }
            .sumOf { it.size }
    }

    private fun parse(input: String): Sequence<Vec2> {
        return input.lineSequence().flatMap { line ->
            line.split(" -> ")
                .map { it.split(",").let { (x, y) -> Vec2(x.toInt(), y.toInt()) } }
                .zipWithNext { a, b ->
                    val delta = (b - a).let { Vec2(it.x.sign, it.y.sign) }
                    generateSequence(a) { it + delta }.takeWhile { it != b } + b
                }
        }.flatten()
    }

    private fun Vec2.fall() = listOf(Vec2(x, y + 1), Vec2(x - 1, y + 1), Vec2(x + 1, y + 1))

}