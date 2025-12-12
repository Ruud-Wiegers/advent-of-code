package adventofcode.y2025

import adventofcode.io.AdventSolution

fun main() {
    Day12.solve()
}

object Day12 : AdventSolution(2025, 12, "Christmas Tree Farm") {

    override fun solvePartOne(input: String): Any {
        val (shapes, puzzle) = parse(input)
        val packingFactor = puzzle.map { shapes.zip(it.counts, Int::times).sum() / it.area.toDouble() }
        return packingFactor.count { it < 1.0 }
    }

    override fun solvePartTwo(input: String) = "Free Star!"
}

private fun parse(input: String): Puzzles {

    val split = input.split("\n\n")
    val shapes = split.dropLast(1).map { it.count { it == '#' } }

    val puzzle = split.last().lines().map {

        val line = it.split(": ", "x", " ").map { it.toInt() }

        val area = line[0] * line[1]

        val shapeCounts = line.drop(2)
        Puzzle(area, shapeCounts)
    }
    return Puzzles(shapes, puzzle)
}

private data class Puzzles(val shapes: List<Int>, val puzzle: List<Puzzle>)
private data class Puzzle(val area: Int, val counts: List<Int>)