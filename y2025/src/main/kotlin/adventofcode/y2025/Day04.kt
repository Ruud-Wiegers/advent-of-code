package adventofcode.y2025

import adventofcode.io.AdventSolution
import adventofcode.util.collections.findFirstDuplicate
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.mooreNeighbors

fun main() = Day04.solve()

object Day04 : AdventSolution(2025, 4, "Printing Department") {

    override fun solvePartOne(input: String): Int {
        val stacks = getStacks(input)
        return stacks.count { it.canBeRemovedFrom(stacks) }
    }

    override fun solvePartTwo(input: String): Int {
        val stacks = getStacks(input)

        val noMoreMoves = generateSequence(stacks) { prev ->
            prev.filterNot { it.canBeRemovedFrom(prev) }.toSet()
        }.findFirstDuplicate()!!

        return stacks.size - noMoreMoves.size
    }

    private fun getStacks(input: String): Set<Vec2> = buildSet {
        input.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                if (ch == '@') add(Vec2(x, y))
            }
        }
    }

    private fun Vec2.canBeRemovedFrom(stacks: Set<Vec2>) = mooreNeighbors().count { it in stacks } < 5
}