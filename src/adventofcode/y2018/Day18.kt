package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve
import kotlin.math.max
import kotlin.math.min

fun main()=Day18.solve()
object Day18 : AdventSolution(2018, 18, "Settlers of The North Pole") {


    override fun solvePartOne(input: String) = generateSequence(ConwayGrid(input), ConwayGrid::next)
            .drop(10)
            .first()
            .score()

    override fun solvePartTwo(input: String): Int {
        generateSequence(seed = ConwayGrid(input)) { it.next()}
                .drop(20000 )
                .take(50)
                .map { it.score() }
                .forEach { println(it) }

        return (1000000000 - 20000) % 28
    }

//20
}

private data class ConwayGrid(private val grid: List<String>) {

    constructor(input: String) : this(input.split("\n"))

    fun score() =grid.sumBy { it.count { it== '|' } }*grid.sumBy { it.count { it== '#' } }

    fun next() = List(grid.size) { y ->
        grid[0].indices.map { x ->
            aliveNext(x, y)
        }.joinToString("")
    }.let(::ConwayGrid)


    private fun aliveNext(x: Int, y: Int): Char {
        return when (grid[y][x]) {
            '.' -> if (area(x, y, '|') >= 3) '|' else '.'
            '|' -> if (area(x, y, '#') >= 3) '#' else '|'
            '#' -> if (area(x, y, '|') > 0 && area(x, y, '#') > 0) '#' else '.'
            else -> '?'
        }

    }

    private fun area(x: Int, y: Int, c: Char): Int {
        var count = 0
        for (i in max(x - 1, 0)..min(x + 1, grid.lastIndex))
            for (j in max(y - 1, 0)..min(y + 1, grid.lastIndex))
                if (grid[j][i] == c) count++
        if (grid[y][x] == c) count--

        return count
    }

}
