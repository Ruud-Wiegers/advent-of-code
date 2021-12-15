package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.util.vector.Vec2
import java.util.PriorityQueue

object Day15 : AdventSolution(2021, 15, "Chitons")
{
    override fun solvePartOne(input: String): Int?
    {
        val grid = parse(input)
        return findPath(Vec2.origin, Vec2(grid.lastIndex, grid[0].lastIndex), grid)
    }

    override fun solvePartTwo(input: String): Any?
    {
        val grid = parse(input)

        val size = grid.size
        val bigGrid = List(size * 5) { y ->
            IntArray(grid[0].size * 5) { x ->
                (grid[y % size][x % size] + y / size + x / size - 1) % 9 + 1
            }
        }
        return findPath(Vec2.origin, Vec2(bigGrid.lastIndex, bigGrid[0].lastIndex), bigGrid)
    }

    private fun parse(input: String) = input.lines().map { it.map { it - '0' }.toIntArray() }
}

private fun findPath(start: Vec2, goal: Vec2, cave: List<IntArray>): Int?
{
    val distances = cave.map { IntArray(it.size) { 1_000_000 } }
    distances[0][0] = 0

    val openList = PriorityQueue(compareBy<Vec2> { distances[it.y][it.x] })
    openList += start

    while (openList.isNotEmpty())
    {
        val c = openList.poll()
        if (c == goal) return distances[c.y][c.x]

        neighbors(cave, c).forEach { n ->
            val newDistance = distances[c.y][c.x] + cave[n.y][n.x]

            if (newDistance < distances[n.y][n.x])
            {
                distances[n.y][n.x] = newDistance
                openList += n
            }
        }
    }

    return null
}

private fun neighbors(cave: List<IntArray>, v: Vec2) = buildList {
    if (v.x > 0) add(Vec2(v.x - 1, v.y))
    if (v.y > 0) add(Vec2(v.x, v.y - 1))
    if (v.x < cave[0].lastIndex) add(Vec2(v.x + 1, v.y))
    if (v.y < cave.lastIndex) add(Vec2(v.x, v.y + 1))
}
