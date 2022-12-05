package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.util.transpose

object Day05 : AdventSolution(2022, 5, "Camp Cleanup") {

    override fun solvePartOne(input: String): String {
        val (bricks, moves) = parse(input)

        val b = bricks.toMutableList()

        moves.forEach { (count, from, to) ->
            b[to - 1] += b[from - 1].takeLast(count).reversed()
            b[from - 1] = b[from - 1].dropLast(count)
        }

        return b.map { it.last() }.joinToString("")
    }

    override fun solvePartTwo(input: String): String {
        val (bricks, moves) = parse(input)

        val b = bricks.toMutableList()

        moves.forEach { (count, from, to) ->
            b[to - 1] += b[from - 1].takeLast(count)
            b[from - 1] = b[from - 1].dropLast(count)
        }

        return b.map { it.last() }.joinToString("")
    }

    private fun parse(input: String): Pair<List<List<Char>>, List<Move>> {

        val (bricks, moves) = input.split("\n\n")

        val parsedBricks = bricks.lines().dropLast(1).map {
            it.chunked(4).map { it[1] }
        }
            .transpose()
            .map { it.reversed().filterNot { it.isWhitespace() } }

        val regex = Regex("""move (\d+) from (\d+) to (\d+)""")
        val parsedMoves = moves.lines()
            .map { regex.matchEntire(it)!!.destructured }
            .map { (c, f, t) -> Move(c.toInt(), f.toInt(), t.toInt()) }

        return parsedBricks to parsedMoves
    }

    private data class Move(val count: Int, val from: Int, val to: Int)
}