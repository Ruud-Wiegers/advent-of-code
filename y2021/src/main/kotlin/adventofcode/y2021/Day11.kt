package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2

fun main() {
    Day11.solve()
}

object Day11 : AdventSolution(2021, 11, "Dumbo Octopus") {
    override fun solvePartOne(input: String) =
        generateSequence(parseInput(input), Grid::next).drop(1).take(100).sumOf(Grid::flashed)

    override fun solvePartTwo(input: String) =
        generateSequence(parseInput(input), Grid::next).map(Grid::flashed).indexOfFirst(100::equals)

    private fun parseInput(input: String) = input.lines()
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, ch -> Vec2(x, y) to Character.getNumericValue(ch) }
        }
        .toMap()
        .let(::Grid)

    private data class Grid(val cells: Map<Vec2, Int>) {

        fun flashed(): Int = cells.count { it.value == 0 }

        fun next(): Grid = generateSequence(grow(), Grid::flash)
            .zipWithNext().first { it.first == it.second }.first

        private fun grow() = Grid(cells = cells.mapValues { it.value + 1 })

        private fun flash() = Grid(cells = cells.mapValues { (k, v) ->
            if (v in 1..9) v + neighbors(k).count { cells.getValue(it) > 9 }
            else 0
        })

        private fun neighbors(v: Vec2) =
            (-1..1).flatMap { y -> (-1..1).map { x -> Vec2(x, y) } }
                .map { it + v }
                .filter { it in cells.keys }
    }
}
