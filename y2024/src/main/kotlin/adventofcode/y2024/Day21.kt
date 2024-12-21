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
    line.dropLast(1).toInt() * solve(line, numpadTransitions, depth)
}


private data class Path(val from: Char, val to: Char)
private data class MemoState(val path: Path, val depth: Int)

private val numpadTransitions = createNumpad().transitions()
private val movepadTransitions = createArrowpad().transitions()

private val memo = mutableMapOf<MemoState, Long>()

private fun solve(inputs: String, transitions: Map<Path, List<String>>, depth: Int) =
    ("A$inputs").zipWithNext { a, b -> Path(a, b) }
        .sumOf { countSteps(transitions, MemoState(it, depth)) }


private fun countSteps(transitions: Map<Path, List<String>>, state: MemoState): Long = memo.getOrPut(state) {
    if (state.depth == 0) 1L
    else {
        transitions.getValue(state.path).minOf { solve(it, movepadTransitions, state.depth - 1) }
    }
}

private data class Keypad(val grid: Map<Char, Vec2>) {
    val validPositions = grid.values.toSet()

    fun validMovement(from: Vec2, moves: String): Boolean {
        return moves.scan(from) { acc, it ->
            when (it) {
                '<' -> acc + Direction.LEFT.vector
                '>' -> acc + Direction.RIGHT.vector
                '^' -> acc + Direction.UP.vector
                'v' -> acc + Direction.DOWN.vector
                else -> acc
            }
        }.all { it in validPositions }
    }

    fun inputsToPress(path: Path): List<String> {
        val (dx, dy) = grid.getValue(path.to) - grid.getValue(path.from)

        val h = buildString {
            if (dx < 0) repeat(-dx) { append('<') }
            if (dx > 0) repeat(dx) { append('>') }
        }

        val v = buildString {
            if (dy < 0) repeat(-dy) { append('^') }
            if (dy > 0) repeat(dy) { append('v') }
        }

        return listOf(h + v + "A", v + h + "A").filter { validMovement(grid.getValue(path.from), it) }
    }

    fun transitions(): Map<Path, List<String>> =
        grid.keys.flatMap { from -> grid.keys.map { to -> Path(from, to) } }.associateWith(::inputsToPress)
}


private fun createNumpad() = Keypad(
    mapOf(
        '7' to Vec2(0, 0), '8' to Vec2(1, 0), '9' to Vec2(2, 0),
        '4' to Vec2(0, 1), '5' to Vec2(1, 1), '6' to Vec2(2, 1),
        '1' to Vec2(0, 2), '2' to Vec2(1, 2), '3' to Vec2(2, 2),
        '0' to Vec2(1, 3), 'A' to Vec2(2, 3)
    )
)

private fun createArrowpad() = Keypad(
    mapOf(
        '^' to Vec2(1, 0), 'A' to Vec2(2, 0),
        '<' to Vec2(0, 1), 'v' to Vec2(1, 1), '>' to Vec2(2, 1)
    )
)
