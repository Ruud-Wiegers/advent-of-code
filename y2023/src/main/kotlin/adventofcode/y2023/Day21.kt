package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors
import adventofcode.util.vector.toGrid

fun main() {
    Day21.solve()
}

object Day21 : AdventSolution(2023, 21, "Pulse Propagation") {

    override fun solvePartOne(input: String): Int {
        val path = parse(input)
        return solve(path.keys, path.filterValues { it == 'S' }.keys.first(), 64)
    }

    fun brute(input: String, steps: Int): Int {
        val path = parse(input)
        return solve(path.keys, path.filterValues { it == 'S' }.keys.first(), steps)

    }


    override fun solvePartTwo(input: String): Long {

        /* notes on the input:
           S is in the middle of a 131*131 plot
           S has a clear view NESW
           the outer edge is clear too
           so we can disregard the interior
           so you can draw a diamond with radius 2mid013mid, and that's more or less correct
           use checkerboarding to count filled squares
           use pathfinding for the perimeter
        */

        val steps = 26501365L
        val length = 131
        return solve(input, length, steps)
    }

    fun solve(input: String, length: Int, steps: Long): Long {
        val path = parse(input).keys

        val pos = SidePositions(length)

        var count = 0L

        val evens = solve(path, pos.middle, length  * 2 + 2).toLong()
        val odds = solve(path, pos.middle, length  * 2 + 1).toLong()
        val bigCount = (steps / length).let { it * it }
        val smallCount = (steps / length - 1).let { it * it }
        val parity = ((steps -2) / length) %2 == 0L
        val stepParity = steps % 2 == 0L

        count += evens *        if(stepParity == parity) bigCount else smallCount
        count += odds *         if(stepParity == parity) smallCount else bigCount

        val a = (steps % length + length/2).toInt()
        val endpoints = listOf(pos.n, pos.e, pos.s, pos.w).map { solve(path, it, a).toLong() }

        count += endpoints.sum()


        val b = ((steps + length - 1) % (length * 2)).toInt()
        val diagonals1 = listOf(pos.nw, pos.ne, pos.se, pos.sw).map { solve(path, it, b).toLong() }
        val diagonal1Count = (steps + length - 1) / length /2*2 -1
        count += diagonal1Count * diagonals1.sum()


        val c = ((steps - 1) % (length * 2)).toInt()
        val diagonals2 = listOf(pos.nw, pos.ne, pos.se, pos.sw).map { solve(path, it, c).toLong() }
        val diagonal2Count = (steps - 1) / length / 2 * 2
        count += diagonal2Count * diagonals2.sum()

        return count
    }


}


data class SidePositions(val length: Int) {
    private val f = 0
    private val m = length / 2
    private val l = length - 1

    val middle = Vec2(m, m)
    val nw = Vec2(f, f)
    val n = Vec2(m, f)
    val ne = Vec2(l, f)
    val e = Vec2(l, m)
    val se = Vec2(l, l)
    val s = Vec2(m, l)
    val sw = Vec2(f, l)
    val w = Vec2(f, m)

}


private fun solve(path: Set<Vec2>, initial: Vec2, steps: Int): Int {
    return generateSequence(setOf(initial)) { reached ->
        reached.flatMap { it.neighbors().filter { it in path } }.toSet()
    }.elementAt(steps).count()
}

private fun parse(input: String): Map<Vec2, Char> = input.lines()
    .flatMapIndexed { y, line ->
        line.withIndex()
            .map { (x, value) -> Vec2(x, y) to value }
    }
    .toMap().filterValues { it != '#' }