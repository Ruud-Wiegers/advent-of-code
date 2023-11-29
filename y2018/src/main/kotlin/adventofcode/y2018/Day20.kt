package adventofcode.y2018

import adventofcode.io.AdventSolution
import java.util.*

object Day20 : AdventSolution(2018, 20, "A Regular Map") {

    override fun solvePartOne(input: String) =
        findDistances(input).values.maxOrNull()

    override fun solvePartTwo(input: String) =
        findDistances(input).count { it.value >= 1000 }

    private fun findDistances(input: String): Map<Point, Int> {
        val distanceTo = mutableMapOf(Point(0, 0) to 0)
        var currentPositions = setOf(Point(0, 0))
        val positionsWhenEnteringIntersection = Stack<Set<Point>>()
        val endPositionsOfBranches = Stack<Set<Point>>()


        for (ch in input) {
            when (ch) {
                in "NESW" -> {
                    currentPositions = currentPositions.map { oldPosition ->
                        oldPosition.step(ch).apply {
                            distanceTo.merge(this, distanceTo.getValue(oldPosition) + 1, ::minOf)
                        }
                    }.toSet()
                }
                '('       -> {
                    endPositionsOfBranches.push(emptySet())
                    positionsWhenEnteringIntersection.push(currentPositions)
                }
                '|'       -> {
                    endPositionsOfBranches.push(endPositionsOfBranches.pop() + currentPositions)
                    currentPositions = positionsWhenEnteringIntersection.peek()
                }
                ')'       -> {
                    currentPositions = endPositionsOfBranches.pop() + currentPositions
                    positionsWhenEnteringIntersection.pop()
                }
            }
        }
        return distanceTo
    }

    private data class Point(val x: Int, val y: Int) {
        fun step(ch: Char) = when (ch) {
            'N'  -> Point(x, y - 1)
            'E'  -> Point(x + 1, y)
            'S'  -> Point(x, y + 1)
            'W'  -> Point(x - 1, y)
            else -> throw IllegalStateException()
        }
    }
}
