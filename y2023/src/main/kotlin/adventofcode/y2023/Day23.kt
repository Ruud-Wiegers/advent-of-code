package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.SparseGrid
import adventofcode.util.vector.Vec2

fun main() {
    Day23.solve()
}

object Day23 : AdventSolution(2023, 23, "A Long Walk") {

    override fun solvePartOne(input: String): Any {

        val lines = input.lines()
        val start = lines.first().indexOfFirst { it == '.' }.let { Vec2(it, 0) }
        val end = lines.last().indexOfFirst { it == '.' }.let { Vec2(it, lines.lastIndex) }
        val grid = parse(input) + (start to 'v') + (end to 'v')

        val paths = findPathLengths(start, grid)
        val lengths = paths.associate { (it.start to it.end) to it.length }
        val pathFrom = paths.groupBy { it.start }

        val exit = end + Direction.DOWN.vector

        return findLongestPath(start, exit, pathFrom, lengths)
    }

    override fun solvePartTwo(input: String): Any {

        val lines = input.lines()
        val start = lines.first().indexOfFirst { it == '.' }.let { Vec2(it, 0) }
        val end = lines.last().indexOfFirst { it == '.' }.let { Vec2(it, lines.lastIndex) }
        val grid = parse(input) + (start to 'v') + (end to 'v')

        val directedPaths = findPathLengths(start, grid)

        val paths = directedPaths + directedPaths.map { it.copy(start = it.end, end = it.start) }
        val lengths = paths.associate { (it.start to it.end) to it.length }
        val pathFrom = paths.groupBy { it.start }

        val exit = end + Direction.DOWN.vector

        return findLongestPath(start, exit, pathFrom, lengths)
    }


    private fun findPathLengths(
        start: Vec2,
        grid: Map<Vec2, Char>
    ): Set<Segment> {
        val segments = mutableSetOf<Segment>()
        val intersections = mutableSetOf<Vec2>()
        val unexplored = mutableListOf<Vec2>()


        val initial = followPath(start, Direction.DOWN, grid)
        segments += initial
        unexplored += initial.end

        while (unexplored.isNotEmpty()) {
            val intersection = unexplored.removeLast()
            if (intersection in intersections) continue
            intersections += intersection
            val newPaths = exits(intersection, grid).map { followPath(intersection, it, grid) }
            segments += newPaths
            unexplored += newPaths.map { it.end }.filter { it !in intersections }
        }
        return segments
    }

    private fun exits(cross: Vec2, grid: SparseGrid<Char>): List<Direction> = buildList {
        if (grid[cross + Direction.UP.vector] == '^') add(Direction.UP)
        if (grid[cross + Direction.RIGHT.vector] == '>') add(Direction.RIGHT)
        if (grid[cross + Direction.DOWN.vector] == 'v') add(Direction.DOWN)
        if (grid[cross + Direction.LEFT.vector] == '<') add(Direction.LEFT)
    }

    private fun followPath(cross: Vec2, dir: Direction, grid: SparseGrid<Char>): Segment {

        val (pathLength, end) = generateSequence(cross + dir.vector + dir.vector to dir) { (pos, dir) ->
            Direction.entries
                .filter { it != dir.reverse }
                .map { pos + it.vector to it }
                .single { it.first in grid }
        }.withIndex().first { grid[it.value.first]!! != '.' }

        return Segment(cross, end.first + end.second.vector, pathLength + 3)
    }


    private fun findLongestPath(
        start: Vec2,
        exit: Vec2,
        pathsFrom: Map<Vec2, List<Segment>>,
        lengths: Map<Pair<Vec2, Vec2>, Int>
    ): Int {
        fun fullPath(path: List<Vec2>): List<List<Vec2>> =
            if (path.last() == exit) listOf(path)
            else pathsFrom[path.last()].orEmpty().map { it.end }.filter { it !in path }
                .flatMap { next -> fullPath(path + next) }


        return fullPath(listOf(start)).maxOf { it.zipWithNext().sumOf { lengths[it] ?: 0 } }
    }

}

private data class Segment(
    val start: Vec2,
    val end: Vec2,
    val length: Int
)


private fun parse(input: String) = input.lines().flatMapIndexed { y, line ->
    line.mapIndexed { x, ch -> Vec2(x, y) to ch }
}.toMap().filterValues { it != '#' }
