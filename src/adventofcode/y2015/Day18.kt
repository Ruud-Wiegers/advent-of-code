package adventofcode.y2015

import adventofcode.AdventSolution
import adventofcode.solve
import kotlin.math.max
import kotlin.math.min

fun main() = Day18.solve()

object Day18 : AdventSolution(2015, 18, "Like a GIF For Your Yard") {

    override fun solvePartOne(input: String) = generateSequence(ConwayGrid(input), ConwayGrid::next)
            .drop(100)
            .first()
            .countAlive()

    override fun solvePartTwo(input: String) = generateSequence(ConwayGrid(input).stuckCorners()) { it.next().stuckCorners() }
            .drop(100)
            .first()
            .countAlive()

}


private data class ConwayGrid(private val grid: List<BooleanArray>) {

    constructor(input: String) : this(input.split("\n").map { it.map { it == '#' }.toBooleanArray() })

    fun next() = List(grid.size) { y ->
        BooleanArray(grid[0].size) { x ->
            aliveNext(x, y)
        }
    }.let(::ConwayGrid)

    fun stuckCorners() = this.copy().apply {
        val last = grid.lastIndex
        grid[0][0] = true
        grid[0][last] = true
        grid[last][0] = true
        grid[last][last] = true
    }

    fun countAlive() = grid.sumBy { it.count { row -> row } }

    private fun aliveNext(x: Int, y: Int) = area(x, y) == 3 || grid[y][x] && area(x, y) == 4

    private fun area(x: Int, y: Int): Int {
        var count = 0
        for (i in max(x - 1, 0)..min(x + 1, grid.lastIndex))
            for (j in max(y - 1, 0)..min(y + 1, grid.lastIndex))
                if (grid[j][i]) count++

        return count
    }

}
