package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

fun main() = Day21.solve()

object Day21 : AdventSolution(2024, 21, "Keypad Conundrum") {
    override fun solvePartOne(input: String) = solve(input, 3)
    override fun solvePartTwo(input: String) = solve(input, 26)
}

private fun solve(input: String, depth: Int): Long = input.lines().sumOf { code ->
    code.dropLast(1).toInt() * countStepsForSequence(numericKeypadPaths, code, depth)
}


private fun countStepsForSequence(keypad: Keypad, buttonSequence: String, depth: Int): Long =
    "A$buttonSequence".zipWithNext(::ButtonPair).sumOf { countStepsForSingleMove(keypad, it, depth) }

private val memo = mutableMapOf<Pair<ButtonPair, Int>, Long>()
private fun countStepsForSingleMove(keypad: Keypad, buttonPair: ButtonPair, depth: Int): Long =
    memo.getOrPut(Pair(buttonPair, depth)) {
        if (depth == 0) 1L
        else countStepsForSequence(directionalKeypadPaths, keypad.bestPath(buttonPair), depth - 1)
    }


private val numericKeypadPaths = Keypad("789", "456", "123", " 0A")
private val directionalKeypadPaths = Keypad(" ^A", "<v>")

private data class Keypad(val grid: Map<Char, Vec2>) {
    constructor(vararg string: String) : this(
        string.flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> c to Vec2(x, y) }
        }
            .filterNot { it.first == ' ' }
            .toMap())

    /* 1. Zigzags are bad, so we only need to consider two alternatives
          Horizontal movement first, or vertical movement first
       2. The < and v buttons are furthest from the A button, so they should be prioritized,
          so they're more clustered. It's slightly magical, but it works
            -> if you go ^<<, the inputting robot has to move left in two separate steps: A -> ^ and ^ -> <
            -> if you go <<^, the inputting robot can move A -> <, which requires two sequential left presses
               and thus saves moving back and forth to the furthest key
       3. Only valid paths should be considered, so sometimes you should take the other order
    */
    fun bestPath(buttonPair: ButtonPair): String {
        val (dx, dy) = grid.getValue(buttonPair.to) - grid.getValue(buttonPair.from)

        fun steps(delta: Int, key: String) = key.repeat(delta.coerceAtLeast(0))

        val l = steps(-dx, "<")
        val r = steps(+dx, ">")
        val u = steps(-dy, "^")
        val d = steps(+dy, "v")

        return listOf(l + d + u + r + "A", r + u + d + l + "A")
            .first { validMovement(grid.getValue(buttonPair.from), it) }
    }

    private fun validMovement(from: Vec2, moves: String): Boolean {
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

}

private data class ButtonPair(val from: Char, val to: Char)