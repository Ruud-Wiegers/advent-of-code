package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve
import java.util.*


fun main() = Day20.solve()

object Day20 : AdventSolution(2018, 20, "A Regular Map") {

    override fun solvePartOne(input: String): Int {
        val neighbors = parseToNeighbors(input)

        //BFS
        var open = setOf(Point(0, 0))
        val closed = mutableSetOf<Point>()
        var count = 0
        while (open.isNotEmpty()) {
            val new = open.map { neighbors[it]!! }.reduce { a, b -> a + b } - closed
            closed += new
            open = new
            count++
        }
        return count - 1
    }

    override fun solvePartTwo(input: String): Int {
        val neighbors = parseToNeighbors(input)

        //BFS
        var open = setOf(Point(0, 0))
        val closed = mutableSetOf<Point>()
        repeat(999) {
            val new = open.map { neighbors[it]!! }.reduce { a, b -> a + b } - closed
            closed += new
            open = new
        }

        return (neighbors.keys - closed).size
    }

    private fun parseToNeighbors(input: String): Map<Point, Set<Point>> {
        val neighbors = mutableMapOf<Point, MutableSet<Point>>()

        //the positions of the current branches we're following
        val restartOnAlternative = ArrayDeque<Set<Point>>()

        //endpoints of subbranches (from which we must continue)
        val continueAfterBranch = ArrayDeque<Set<Point>>()
        var currentPositions = setOf(Point(0, 0))

        for (ch in input) {
            when (ch) {
                in "NESW" -> {
                    //This demonstrates that the input is waaaay nicer than the problem description requires
                    //With this assumption, we would only need a single stack of single positions
                    check(currentPositions.size == 1)

                    val newPositions = mutableSetOf<Point>()
                    currentPositions.forEach { room ->
                                val n = room.step(ch)

                                neighbors.getOrPut(room) { mutableSetOf() }.add(n)
                                neighbors.getOrPut(n) { mutableSetOf() }.add(room)
                                newPositions.add(n)
                            }
                    currentPositions = newPositions
                }
                '(' -> {
                    restartOnAlternative.push(currentPositions)
                    continueAfterBranch.push(emptySet())
                }
                '|' -> {
                    continueAfterBranch.push(continueAfterBranch.pop() + currentPositions)
                    currentPositions = restartOnAlternative.peek()
                }
                ')' -> {
                    restartOnAlternative.pop()
                    currentPositions = continueAfterBranch.pop() + currentPositions
                }
            }
        }
        return neighbors
    }

    private data class Point(val x: Int, val y: Int)

    private fun Point.step(ch: Char) = when (ch) {
        'N' -> Point(x, y - 1)
        'E' -> Point(x + 1, y)
        'S' -> Point(x, y + 1)
        'W' -> Point(x - 1, y)
        else -> throw IllegalStateException()
    }
}