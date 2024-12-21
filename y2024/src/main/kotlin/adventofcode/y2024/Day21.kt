package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

fun main() = Day21.solve()

object Day21 : AdventSolution(2024, 21, "Keypad Conundrum") {
    override fun solvePartOne(input: String) = solve(input, 3)
    override fun solvePartTwo(input: String) = solve(input, 26)
}

private fun solve(input: String, depth: Int): Long = input.lines().sumOf { line ->
    line.dropLast(1).toInt() * countSteps(numpadPossiblePaths, line, depth)
}


private data class ButtonPair(val from: Char, val to: Char)
private val memo = mutableMapOf<Pair<ButtonPair, Int>, Long>()


private fun countSteps(possiblePaths: Map<ButtonPair, List<String>>, buttonSequence: String, depth: Int) =
    ("A$buttonSequence").zipWithNext { a, b -> ButtonPair(a, b) }
        .sumOf { countSteps(possiblePaths, it, depth) }


private fun countSteps(possiblePaths: Map<ButtonPair, List<String>>, buttonPair: ButtonPair, depth: Int): Long =
    memo.getOrPut(Pair(buttonPair, depth)) {
        if (depth == 0) 1L
        else possiblePaths.getValue(buttonPair).minOf { countSteps(movepadPossiblePaths, it, depth - 1) }
    }


private val numpadPossiblePaths = Keypad(
    mapOf(
        '7' to Vec2(0, 0), '8' to Vec2(1, 0), '9' to Vec2(2, 0),
        '4' to Vec2(0, 1), '5' to Vec2(1, 1), '6' to Vec2(2, 1),
        '1' to Vec2(0, 2), '2' to Vec2(1, 2), '3' to Vec2(2, 2),
        '0' to Vec2(1, 3), 'A' to Vec2(2, 3)
    )
).possiblePaths()

private val movepadPossiblePaths = Keypad(
    mapOf(
        '^' to Vec2(1, 0), 'A' to Vec2(2, 0),
        '<' to Vec2(0, 1), 'v' to Vec2(1, 1), '>' to Vec2(2, 1)
    )
).possiblePaths()


private data class Keypad(val grid: Map<Char, Vec2>) {

    fun validMovement(from: Vec2, moves: String): Boolean {
        return moves.scan(from) { acc, it ->
            when (it) {
                '<' -> acc + Direction.LEFT.vector
                '>' -> acc + Direction.RIGHT.vector
                '^' -> acc + Direction.UP.vector
                'v' -> acc + Direction.DOWN.vector
                else -> acc
            }
        }.all { it in grid.values }
    }

    fun possiblePaths(buttonPair: ButtonPair): List<String> {
        val (dx, dy) = grid.getValue(buttonPair.to) - grid.getValue(buttonPair.from)

        val h = buildString {
            if (dx < 0) repeat(-dx) { append('<') }
            if (dx > 0) repeat(dx) { append('>') }
        }

        val v = buildString {
            if (dy < 0) repeat(-dy) { append('^') }
            if (dy > 0) repeat(dy) { append('v') }
        }

        return listOf(h + v + "A", v + h + "A").filter { validMovement(grid.getValue(buttonPair.from), it) }
    }

    fun possiblePaths(): Map<ButtonPair, List<String>> =
        grid.keys.flatMap { from -> grid.keys.map { to -> ButtonPair(from, to) } }.associateWith(::possiblePaths)
}