package adventofcode.y2022

import adventofcode.io.AdventSolution
import adventofcode.util.transposeString


object Day05 : AdventSolution(2022, 5, "Camp Cleanup") {

    override fun solvePartOne(input: String): String {
        val (bricks, moves) = parse(input)

        val b = bricks.toMutableList()

        moves.forEach { (count, from, to) ->
            b[to] = b[from].take(count).reversed() + b[to]
            b[from] = b[from].drop(count)
        }

        return b.joinToString("") { it.first().toString() }
    }

    override fun solvePartTwo(input: String): String {
        val (bricks, moves) = parse(input)

        val b = bricks.toMutableList()

        moves.forEach { (count, from, to) ->
            b[to] = b[from].take(count) + b[to]
            b[from] = b[from].drop(count)
        }

        return b.joinToString("") { it.first().toString() }
    }

    private fun parse(input: String): Pair<List<String>, List<Move>> {

        val (bricks, moves) = input.split("\n\n")

        val parsedBricks = bricks.lines().dropLast(1)
            .map { it.slice(1..it.lastIndex step 4) }
            .transposeString()
            .map(String::trimStart)

        val regex = Regex("""move (\d+) from (\d+) to (\d+)""")
        val parsedMoves = moves.lines().map { regex.matchEntire(it)!!.groupValues.drop(1).map(String::toInt) }
            .map { (count, from, to) -> Move(count, from - 1, to - 1) }

        return parsedBricks to parsedMoves
    }

    private data class Move(val count: Int, val from: Int, val to: Int)
}