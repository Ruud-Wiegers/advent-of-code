package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.y2017.takeWhileDistinct
import kotlin.math.max
import kotlin.math.min


object Day18 : AdventSolution(2018, 18, "Settlers of The North Pole") {


    override fun solvePartOne(input: String) = generateSequence(ConwayGrid(input), ConwayGrid::next)
            .drop(10)
            .first()
            .score()

    override fun solvePartTwo(input: String): Int {
        val (steps, state) = generateSequence(ConwayGrid(input)) { it.next() }
                .takeWhileDistinct()
                .withIndex().last()

        val cycle = generateSequence(state) { it.next() }
                .takeWhileDistinct()
                .count()

        val r = (1000000000 - steps) % cycle
        return generateSequence(state) { it.next() }
                .drop(r)
                .first()
                .score()

    }

}

private data class ConwayGrid(private val grid: List<String>) {

    constructor(input: String) : this(input.split("\n"))

    fun score() = grid.sumBy { it.count { it == '|' } } * grid.sumBy { it.count { it == '#' } }

    fun next() = List(grid.size) { y ->
        grid[0].indices.map { x ->
            aliveNext(x, y)
        }.joinToString("")
    }.let(::ConwayGrid)


    private fun aliveNext(x: Int, y: Int): Char {
        return when (grid[y][x]) {
            '.' -> if (neighbors(x, y, '|') >= 3) '|' else '.'
            '|' -> if (neighbors(x, y, '#') >= 3) '#' else '|'
            '#' -> if (neighbors(x, y, '|') > 0 && neighbors(x, y, '#') > 0) '#' else '.'
            else -> '?'
        }

    }

    private fun neighbors(x: Int, y: Int, c: Char): Int {
        var count = 0
        for (i in max(x - 1, 0)..min(x + 1, grid.lastIndex))
            for (j in max(y - 1, 0)..min(y + 1, grid.lastIndex))
                if (grid[j][i] == c) count++
        if (grid[y][x] == c) count--

        return count
    }
}
