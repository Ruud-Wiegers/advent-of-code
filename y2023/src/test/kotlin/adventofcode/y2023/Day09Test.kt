package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day09Test {

    @Test
    fun solvePartOne() {
        val input = """10 13 16 21 30 45"""

        assertEquals(68, Day09.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        val input = """10 13 16 21 30 45""".trimIndent()

        assertEquals(5, Day09.solvePartTwo(input))
    }

}