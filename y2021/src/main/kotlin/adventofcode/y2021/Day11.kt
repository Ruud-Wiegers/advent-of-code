package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2

fun main() {
    Day11.solve()
}

object Day11 : AdventSolution(2021, 11, "Dumbo Octopus") {
    override fun solvePartOne(input: String) =
        generateSequence(parseInput(input), MutableGrid::next).drop(1).take(100).sumOf(MutableGrid::flashed)

    override fun solvePartTwo(input: String) =
        generateSequence(parseInput(input), MutableGrid::next).map(MutableGrid::flashed).indexOfFirst(100::equals)


    private fun parseInput(input: String) = input.lines()
        .map { line -> line.map { Character.getNumericValue(it) }.toMutableList() }
        .let(::MutableGrid)

    private data class MutableGrid(val grid: List<MutableList<Int>>) {
        fun next(): MutableGrid {
            val g = MutableGrid(grid = grid.map { it.map { it + 1 }.toMutableList() })
            val open = g.covering().filter { g[it] > 9 }.toMutableList()
            val closed = mutableSetOf<Vec2>()
            while (open.isNotEmpty()) {
                val candidate = open.removeLast()
                if (candidate in closed) continue
                closed += candidate
                val n = neighbors(candidate)
                n.forEach { v -> g[v]++ }
                open += n.filter { g[it] > 9 }
            }
            closed.forEach { g[it] = 0 }
            return g
        }

        operator fun get(v: Vec2) = grid[v.y][v.x]
        operator fun set(v: Vec2, new: Int) {
            grid[v.y][v.x] = new
        }

        fun covering() = grid.indices.flatMap { y -> grid[0].indices.map { x -> Vec2(x, y) } }
        fun neighbors(v: Vec2) =
            (-1..1).flatMap { y -> (-1..1).map { x -> Vec2(x, y) } }.map { it + v }
                .filter { (x, y) -> y in grid.indices && x in grid[0].indices }

        fun flashed(): Int = grid.flatten().count { it == 0 }
    }
}
