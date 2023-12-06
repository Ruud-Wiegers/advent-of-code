package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day06Test {

    @Test
    fun solvePartOne() {
        assertEquals(288, Day06.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(71503, Day06.solvePartTwo(input))
    }

    private val input = """
        Time:      7  15   30
        Distance:  9  40  200
        """.trimIndent()
}