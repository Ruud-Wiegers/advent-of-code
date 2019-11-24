package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.y2017.takeWhileDistinct

object Day18 : AdventSolution(2018, 18, "Settlers of The North Pole") {

    override fun solvePartOne(input: String) =
            generateSequence(ConwayGrid(input), ConwayGrid::next)
                    .drop(10)
                    .first()
                    .resourceValue()

    override fun solvePartTwo(input: String): Int {
        val (steps, state) = generateSequence(ConwayGrid(input), ConwayGrid::next)
                .takeWhileDistinct()
                .withIndex()
                .last()

        val cycleLength = generateSequence(state, ConwayGrid::next)
                .takeWhileDistinct()
                .count()

        val remainingSteps = (1_000_000_000 - steps) % cycleLength
        return generateSequence(state, ConwayGrid::next)
                .drop(remainingSteps)
                .first()
                .resourceValue()
    }
}

private data class ConwayGrid(private val grid: List<List<Char>>) {

    constructor(input: String) : this(input.lines().map(String::toList))

    fun resourceValue() = count('|') * count('#')

    private fun count(ch: Char) = grid.flatten().count { it == ch }

    fun next() = grid.indices.map { y ->
        grid[0].indices.map { x ->
            typeInNextGeneration(x, y)
        }
    }.let(::ConwayGrid)

    private fun typeInNextGeneration(x: Int, y: Int): Char =
            when (grid[y][x]) {
                '.' -> if (countNear(x, y, '|') >= 3) '|' else '.'
                '|' -> if (countNear(x, y, '#') >= 3) '#' else '|'
                '#' -> if (countNear(x, y, '|') == 0 || countNear(x, y, '#') <= 1) '.' else '#'
                else -> throw IllegalStateException()
            }

    private fun countNear(x: Int, y: Int, c: Char): Int {
        var count = 0
        for (j in y - 1..y + 1)
            for (i in x - 1..x + 1)
                if (grid.getOrNull(j)?.getOrNull(i) == c)
                    count++
        return count
    }
}
