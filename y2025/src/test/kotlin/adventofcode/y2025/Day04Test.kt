package adventofcode.y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day04Test {

    @Test
    fun solvePartOne() {
        assertEquals(13, Day04.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(43, Day04.solvePartTwo(input))
    }

    private val input = """..@@.@@@@.
    @@@.@.@.@@
    @@@@@.@.@@
    @.@@@@..@.
    @@.@@@@.@@
    .@@@@@@@.@
    .@.@.@.@@@
    @.@@@.@@@@
    .@@@@@@@@.
    @.@.@@@.@."""
}