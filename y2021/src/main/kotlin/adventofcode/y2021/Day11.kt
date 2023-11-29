package adventofcode.y2021

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.mooreNeighbors

object Day11 : AdventSolution(2021, 11, "Dumbo Octopus") {
    override fun solvePartOne(input: String) =
        generateSequence(parseInput(input), Grid::step).drop(1).take(100).sumOf(Grid::flashed)

    override fun solvePartTwo(input: String) =
        generateSequence(parseInput(input), Grid::step).map(Grid::flashed).indexOfFirst(100::equals)

    private fun parseInput(input: String) = input.lines()
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, ch -> Vec2(x, y) to Character.getNumericValue(ch) }
        }
        .toMap().let(::Grid)

    private class Grid(val octopuses: Map<Vec2, Int>) {

        fun flashed(): Int = octopuses.count { it.value == 0 }

        fun step(): Grid = generateSequence(charge(), Grid::flash).first(Grid::done)

        private fun charge() = Grid(octopuses.mapValues { it.value + 1 })

        private fun flash() = Grid(octopuses.mapValues { (pos, v) ->
            if (v in 1..9) v + pos.mooreNeighbors().count { (octopuses[it] ?: 0) > 9 }
            else 0
        })

        private fun done() = octopuses.none { it.value > 9 }
    }
}
