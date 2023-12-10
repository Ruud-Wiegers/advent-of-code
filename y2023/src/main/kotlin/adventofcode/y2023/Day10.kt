package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Direction.*
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.toGrid

fun main() {
    Day10.solve()
}

object Day10 : AdventSolution(2023, 10, "Pipe Maze") {

    override fun solvePartOne(input: String): Any? {

        val grid = input.lines().flatMapIndexed { y, line ->
            line.mapIndexed { x, ch -> Vec2(x, y) to ch }
        }.toMap()

        val start = grid.filterValues { it == 'S' }.keys.first()


        val direction = Direction.entries.first { it.reverse in grid[start + it.vector]!!.let(edges::getValue) }

        val seq = generateSequence(Pair(start, direction)) { (pos, dir) ->
            val newPos = pos + dir.vector
            val newDir = grid[newPos]!!.let(edges::getValue).first { it != dir.reverse }
            newPos to newDir
        }


        val result = seq.drop(1).indexOfFirst { grid[it.first] == 'S' }

        return (result + 1) / 2
    }

    override fun solvePartTwo(input: String): Int {

        val grid = input.lines().flatMapIndexed { y, line ->
            line.mapIndexed { x, ch -> Vec2(x, y) to ch }
        }.toMap()

        val startPosition = grid.filterValues { it == 'S' }.keys.first()

        val connections = Direction.entries.filter { it.reverse in (grid[startPosition + it.vector]?.let { edges[it]} ?: emptyList()) }.toSet()


        val seq = generateSequence(Pair(startPosition, connections.first())) { (pos, dir) ->
            val newPos = pos + dir.vector
            val newDir = edges[grid[newPos]!!]!!.first { it != dir.reverse }
            newPos to newDir
        }


        val pipes = seq.drop(1).takeWhile { grid[it.first] != 'S' }.map { it.first }.toSet()

        val startSymbol = edges.entries.first { it.value == connections }.key
        val pipeMap = grid.filterKeys { it in pipes } + (startPosition to startSymbol)

        //pretend we're standing on the 'lower half' of a tile, moving right.
        //these are the pipe shapes where we cross from inside/outside the loop
        val pipeCrossing = edges.filterValues { DOWN in it }.keys.joinToString("")

        return pipeMap.toGrid(' ').flatMap { row ->

            //scan the row, keeping track of whether we're on an open space and whether we're inside or outside the loop
            row.scan(Pair(true, false)) { (_, isInside), ch ->
                Pair(ch == ' ', if (ch in pipeCrossing) !isInside else isInside)
            }
        }
            .count { (isOpenSpace, isInside) -> isOpenSpace && isInside }
    }

}

private val edges = mapOf(
    '|' to setOf(UP, DOWN),
    '-' to setOf(LEFT, RIGHT),
    'L' to setOf(UP, RIGHT),
    'J' to setOf(UP, LEFT),
    '7' to setOf(DOWN, LEFT),
    'F' to setOf(DOWN, RIGHT),
    '.' to emptySet(),
    'S' to setOf(UP, DOWN, LEFT, RIGHT) //ugly hack for last step in traversal
)