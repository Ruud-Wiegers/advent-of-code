package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.vector.Vec2
import kotlin.math.absoluteValue

fun main() = Day03.solve()

object Day03 : AdventSolution(2019, 3, "Crossed Wires") {

    override fun solvePartOne(input: String): Int? {
        val (a, b) = input.lines().map(this::parseWire)

        val visited = a.toSet()

        return b.filter { it in visited }
            .map { it.x.absoluteValue + it.y.absoluteValue }
            .minOrNull()
    }

    override fun solvePartTwo(input: String): Int? {
        val (a, b) = input.lines().map(this::parseWire)

        val visited = mutableMapOf<Vec2, Int>()
        a.forEachIndexed { d, p -> visited.putIfAbsent(p, d) }

        return b.withIndex()
            .mapNotNull { (dist, v) -> visited[v]?.let { it + dist + 2 } }
            .minOrNull()
    }

    private fun parseWire(input: String) = input.splitToSequence(',')
            .flatMap { it.substring(0, 1).repeat(it.substring(1).toInt()).asSequence() }
            .scan(Vec2(0, 0)) { p, ch ->
                when (ch) {
                    'U' -> p.copy(x = p.x - 1)
                    'D' -> p.copy(x = p.x + 1)
                    'L' -> p.copy(y = p.y - 1)
                    'R' -> p.copy(y = p.y + 1)
                    else -> throw IllegalStateException()
                }
            }

}
