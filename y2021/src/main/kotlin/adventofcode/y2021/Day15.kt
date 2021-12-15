package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2
import java.util.*


fun main() {
    Day15.solve()
}

object Day15 : AdventSolution(2021, 15, "Chitons") {
    override fun solvePartOne(input: String): Int? {
        val grid = parse(input)

        return findPath(Vec2.origin, Vec2(grid.lastIndex, grid.first().lastIndex), grid)

    }

    override fun solvePartTwo(input: String): Any? {
        val grid = parse(input)

        val size = grid.size
        val bigGrid = List(size * 5) { y ->
            List(grid[0].size * 5) { x ->
                val risk = grid[y % size][x % size] + y / size + x / size
                if (risk > 9) risk - 9 else risk
            }
        }
        return findPath(Vec2.origin, Vec2(bigGrid.lastIndex, bigGrid[0].lastIndex), bigGrid)

    }

    private fun parse(input: String) = input.lines().map { it.map { it - '0' } }

}

private fun findPath(start: Vec2, goal: Vec2, cave: List<List<Int>>): Int? {
    val openList = PriorityQueue(compareBy<State>(State::c))
    openList += State(start, 0)
    val closed = mutableSetOf(start)

    while (openList.isNotEmpty()) {

        val candidate = openList.poll()
        if (candidate.v == goal)
            return candidate.c

        openList.addAll(neighbors(cave, candidate).filter { closed.add(it.v) })

    }
    return null
}

private data class State(val v: Vec2, val c: Int)

private fun neighbors(cave: List<List<Int>>, st: State): List<State> {

    val v = st.v
    val list = mutableListOf<Vec2>()
    if (v.x > 0) list.add(Vec2(v.x - 1, v.y))
    if (v.y > 0) list.add(Vec2(v.x, v.y - 1))
    if (v.x < cave[0].lastIndex) list.add(Vec2(v.x + 1, v.y))
    if (v.y < cave.lastIndex) list.add(Vec2(v.x, v.y + 1))

    return list.map { State(it, cave[it.y][it.x] + st.c) }

}

