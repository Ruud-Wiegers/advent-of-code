package adventofcode.y2016

import adventofcode.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import java.lang.IllegalArgumentException


object Day02 : AdventSolution(2016, 2, "Bathroom Security") {

    override fun solvePartOne(input: String): String {
        val squarePad = """
    123
    456
    789
"""
        val map = convertToPad(squarePad)
        val initial = map.firstNotNullOf { (p, ch) -> p.takeIf { ch == "5" } }
        return solve(input, map, initial)
    }

    override fun solvePartTwo(input: String): String {
        val diamondPad = """
        1
       234
      56789 
       ABC
        D
"""

        val map = convertToPad(diamondPad)
        val initial = map.firstNotNullOf { (p, ch) -> p.takeIf { ch == "5" } }
        return solve(input, map, initial)
    }
}

private fun solve(input: String, pad: Map<Vec2, String>, initial: Vec2) = input
    .lineSequence()
    .scan(initial) { startingButton, instructionLine ->
        instructionLine.map(Char::toDirection).fold(startingButton) { pos, instr ->
            (pos + instr).takeIf { it in pad.keys } ?: pos
        }
    }
    .drop(1)
    .joinToString("", transform = pad::getValue)


private fun Char.toDirection(): Vec2 = when (this) {
    'U' -> Direction.UP
    'D' -> Direction.DOWN
    'L' -> Direction.LEFT
    'R' -> Direction.RIGHT
    else -> throw IllegalArgumentException()
}.vector


private fun convertToPad(input: String) = buildMap {
    input.lines().forEachIndexed { y, line ->
        line.forEachIndexed { x, ch ->
            if (ch.isLetterOrDigit()) put(Vec2(x, y), ch.toString())
        }
    }
}
