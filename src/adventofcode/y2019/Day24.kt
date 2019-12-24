package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.y2017.takeWhileDistinct

fun main() {
    Day24.solve()
}

object Day24 : AdventSolution(2019, 24, "Planet of Discord") {

    override fun solvePartOne(input: String) =
            generateSequence(ConwayGrid(input), ConwayGrid::next)
                    .takeWhileDistinct()
                    .last()
                    .next()
                    .resourceValue()

    override fun solvePartTwo(input: String) = generateSequence(ErisianGrid(input), ErisianGrid::next)
            .drop(200)
            .first()
            .grid
            .flatten()
            .flatten()
            .count { it }
}

private data class ErisianGrid(val grid: List<List<List<Boolean>>>) {
    constructor(input: String) : this(
            List(200) { List(5) { List(5) { false } } } +
                    listOf(input.lines().map { it.map { it == '#' } }) +
                    List(200) { List(5) { List(5) { false } } })

    fun next() = grid.indices.map { l ->
        grid[0].indices.map { y ->
            grid[0][0].indices.map { x ->
                next(l, y, x)
            }
        }
    }.let(::ErisianGrid)

    private fun next(l: Int, y: Int, x: Int): Boolean = if (grid[l][y][x])
        neighbors(l, y, x).count { it } == 1
    else
        neighbors(l, y, x).count { it } in 1..2


    private fun neighbors(l: Int, y: Int, x: Int): List<Boolean> = neighborsL(l, y, x) + neighborsR(l, y, x) + neighborsU(l, y, x) + neighborsD(l, y, x)

    private fun neighborsL(l: Int, y: Int, x: Int): List<Boolean> = when {
        x == 0           -> grid.getOrNull(l - 1)?.let { listOf(it[2][1]) }.orEmpty()
        x == 2 && y == 2 -> emptyList()
        x == 3 && y == 2 -> grid.getOrNull(l + 1).orEmpty().map { it.last() }
        else             -> listOf(grid[l][y][x - 1])
    }


    private fun neighborsR(l: Int, y: Int, x: Int): List<Boolean> = when {
        x == 4           -> grid.getOrNull(l - 1)?.let { listOf(it[2][3]) }.orEmpty()
        x == 2 && y == 2 -> emptyList()
        x == 1 && y == 2 -> grid.getOrNull(l + 1).orEmpty().map { it.first() }
        else             -> listOf(grid[l][y][x + 1])
    }

    private fun neighborsU(l: Int, y: Int, x: Int): List<Boolean> = when {
        y == 0           -> grid.getOrNull(l - 1)?.let { listOf(it[1][2]) }.orEmpty()
        x == 2 && y == 2 -> emptyList()
        x == 2 && y == 3 -> grid.getOrNull(l + 1)?.last().orEmpty()
        else             -> listOf(grid[l][y - 1][x])
    }

    private fun neighborsD(l: Int, y: Int, x: Int): List<Boolean> = when {
        y == 4           -> grid.getOrNull(l - 1)?.let { listOf(it[3][2]) }.orEmpty()
        x == 2 && y == 2 -> emptyList()
        x == 2 && y == 1 -> grid.getOrNull(l + 1)?.first().orEmpty()
        else             -> listOf(grid[l][y + 1][x])
    }
}

private data class ConwayGrid(private val grid: List<List<Char>>) {

    constructor(input: String) : this(input.lines().map(String::toList))

    fun resourceValue() = grid.flatten().map { if (it == '#') 1L else 0L }.reversed().fold(0L) { acc, it -> acc * 2 + it }

    fun next() = grid.indices.map { y ->
        grid[0].indices.map { x ->
            typeInNextGeneration(x, y)
        }
    }.let(::ConwayGrid)

    private fun typeInNextGeneration(x: Int, y: Int): Char =
            when (grid[y][x]) {
                '.'  -> if (countNear(x, y, '#') in 1..2) '#' else '.'
                '#'  -> if (countNear(x, y, '#') != 2) '.' else '#'
                else -> throw IllegalStateException()
            }


    private fun countNear(x: Int, y: Int, c: Char): Int {
        var count = 0
        if (grid.getOrNull(y - 1)?.getOrNull(x) == c)
            count++
        if (grid.getOrNull(y + 1)?.getOrNull(x) == c)
            count++
        for (i in x - 1..x + 1)
            if (grid.getOrNull(y)?.getOrNull(i) == c)
                count++
        return count
    }
}
