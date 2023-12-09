package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day09Test {

    @Test
    fun solvePartOne() {
        assertEquals(68, Day09.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(5, Day09.solvePartTwo(input))
    }

    private val input = "10 13 16 21 30 45"
}